package com.exampleinyection.clase2parte2.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class UserTest {
    @Test
    void testUser() {
        User u = new User(1L, "Pepe", 20, null);
        assertEquals(1L, u.getId());
        assertEquals("Pepe", u.getNombre());
        assertEquals(20, u.getEdad());
        assertEquals(null, u.getAllergy());
        u.setId(2L);
        u.setNombre("Paco");
        u.setEdad(22);
        u.setAllergy(java.util.List.of());
        assertEquals(2L, u.getId());
        assertEquals("Paco", u.getNombre());
        assertEquals(22, u.getEdad());
        u.toString();
        u.hashCode();
        u.equals(new User(2L, "Paco", 22, java.util.List.of()));
    }
}
