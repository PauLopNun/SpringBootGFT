package com.exampleinyection.clase2parte2.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> respuestaLimpia(int status, String mensaje) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status);
        body.put("message", mensaje);
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(UserNotFoundException e) {
        return respuestaLimpia(404, e.getMessage());
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(InvalidUserException e) {
        return respuestaLimpia(417, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
        String mensaje = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return respuestaLimpia(400, mensaje);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(ResponseStatusException e) {
        return respuestaLimpia(e.getStatusCode().value(), e.getReason());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericError(Exception e) {
        return respuestaLimpia(500, "Error interno inesperado");
    }
}