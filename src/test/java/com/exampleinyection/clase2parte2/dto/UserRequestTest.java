package com.exampleinyection.clase2parte2.dto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class UserRequestTest {
    @Test
    void testUserRequest() {
        UserRequest req = new UserRequest("Pepe", 20, null);
        assertEquals("Pepe", req.nombre());
        assertEquals(20, req.edad());
        assertEquals(null, req.allergy());
    }
}
