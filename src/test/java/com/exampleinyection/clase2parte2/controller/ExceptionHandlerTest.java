package com.exampleinyection.clase2parte2.controller;

import com.exampleinyection.clase2parte2.config.AppConfig;
import com.exampleinyection.clase2parte2.dto.UserRequest;
import com.exampleinyection.clase2parte2.exception.ErrorResponse;
import com.exampleinyection.clase2parte2.exception.GlobalExceptionHandler;
import com.exampleinyection.clase2parte2.exception.InvalidUserException;
import com.exampleinyection.clase2parte2.exception.UserNotFoundException;
import com.exampleinyection.clase2parte2.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class ExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AppConfig appConfig;

    @Test
    void triggerInvalidUser() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new InvalidUserException("Invalid"));

        mockMvc.perform(get("/api/1"))
                .andExpect(status().is(417));
    }

    @Test
    void triggerUserNotFound() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new UserNotFoundException("Not found"));

        mockMvc.perform(get("/api/1"))
                .andExpect(status().is(404));
    }

    @Test
    void triggerResponseStatusException() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Reason"));

        mockMvc.perform(get("/api/1"))
                .andExpect(status().is(502));
    }

    @Test
    void triggerGenericException() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new RuntimeException("Generic error"));

        mockMvc.perform(get("/api/1"))
                .andExpect(status().is(500));
    }

    @Test
    void triggerMethodArgumentNotValid() throws Exception {
        BindException bindException = new BindException(new Object(), "userRequest");
        bindException.addError(new ObjectError("userRequest", "Validation failed"));

        MethodParameter methodParameter = new MethodParameter(
                UserController.class.getMethod("createUser", UserRequest.class), 0);

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindException);

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<ErrorResponse> response = handler.handleValidation(exception);

        Assertions.assertEquals(400, response.getStatusCode().value());
        Assertions.assertEquals("Validation failed", response.getBody().getMessage());
    }
}
