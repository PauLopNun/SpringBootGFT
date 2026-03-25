package com.exampleinyection.clase2parte2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(UserNotFoundException e) {

        return crearRespuesta(HttpStatus.NOT_FOUND, "Recurso no encontrado", e.getMessage());
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<Object> handleBadRequest(InvalidUserException e) {

        return crearRespuesta(HttpStatus.BAD_REQUEST, "Error de validación", e.getMessage());

    }

    private ResponseEntity<Object> crearRespuesta(HttpStatus status, String error, String mensaje) {

        Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", mensaje);

        return new ResponseEntity<>(body, status);
    }
}