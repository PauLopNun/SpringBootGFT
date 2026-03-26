package com.exampleinyection.clase2parte2.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class AllergyTest {
    @Test
    void testAllergy() {
        Allergy a = new Allergy("Polen", 5);
        assertEquals("Polen", a.getNombre());
        assertEquals(5, a.getGravity());
        a.setNombre("Polvo");
        a.setGravity(2);
        assertEquals("Polvo", a.getNombre());
        assertEquals(2, a.getGravity());
        a.toString();
        a.hashCode();
        a.equals(new Allergy("Polvo", 2));
    }
}
