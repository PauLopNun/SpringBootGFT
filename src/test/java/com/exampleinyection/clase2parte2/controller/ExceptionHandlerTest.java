package com.exampleinyection.clase2parte2.controller;
import com.exampleinyection.clase2parte2.config.AppConfig;
import com.exampleinyection.clase2parte2.exception.InvalidUserException;
import com.exampleinyection.clase2parte2.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
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
}
