package com.exampleinyection.clase2parte2.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserRequestTest {

    @Test
    void testUserRequest() {
        UserRequest userRequest = new UserRequest("Pepe", 20, null);

        assertEquals("Pepe", userRequest.name());
        assertEquals(20, userRequest.age());
        assertNull(userRequest.allergies());
    }
}
