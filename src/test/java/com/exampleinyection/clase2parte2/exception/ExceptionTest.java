package com.exampleinyection.clase2parte2.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionTest {

    @Test
    void testExceptions() {
        UserNotFoundException notFoundException = new UserNotFoundException("Not found");
        assertEquals("Not found", notFoundException.getMessage());
        
        InvalidUserException invalidUserException = new InvalidUserException("Invalid");
        assertEquals("Invalid", invalidUserException.getMessage());
    }
}
