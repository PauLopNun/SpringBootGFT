package com.exampleinyection.clase2parte2.exception;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class ExceptionTest {
    @Test
    void testExceptions() {
        UserNotFoundException e1 = new UserNotFoundException("Not found");
        assertEquals("Not found", e1.getMessage());
        InvalidUserException e2 = new InvalidUserException("Invalid");
        assertEquals("Invalid", e2.getMessage());
    }
}
