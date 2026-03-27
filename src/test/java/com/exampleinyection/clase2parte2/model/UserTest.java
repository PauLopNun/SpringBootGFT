package com.exampleinyection.clase2parte2.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private static class UserChild extends User {
        UserChild(Long id, String name, int age, List<Allergy> allergies) {
            super(id, name, age, allergies);
        }
    }

    private static class UserChildStrict extends User {
        UserChildStrict(Long id, String name, int age, List<Allergy> allergies) {
            super(id, name, age, allergies);
        }

        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }

    @Test
    void gettersAndSetters() {
        User user = new User(1L, "Pepe", 20, null);

        assertEquals(1L, user.getId());
        assertEquals("Pepe", user.getName());
        assertEquals(20, user.getAge());
        assertNull(user.getAllergies());

        user.setId(2L);
        user.setName("Paco");
        user.setAge(22);
        user.setAllergies(List.of());

        assertEquals(2L, user.getId());
        assertEquals("Paco", user.getName());
        assertEquals(22, user.getAge());
        assertEquals(List.of(), user.getAllergies());
    }

    @Test
    void equalsAndHashCodeCoverBranches() {
        List<Allergy> allergies = List.of(new Allergy("Pollen", 2));

        User base = new User(1L, "Pepe", 20, allergies);
        User sameValues = new User(1L, "Pepe", 20, List.of(new Allergy("Pollen", 2)));
        User nullAllergiesA = new User(1L, "Pepe", 20, null);
        User nullAllergiesB = new User(1L, "Pepe", 20, null);
        User nullFieldsA = new User(null, null, 20, null);
        User nullFieldsB = new User(null, null, 20, null);

        assertEquals(base, base);
        assertEquals(base, sameValues);
        assertEquals(base.hashCode(), sameValues.hashCode());

        assertEquals(nullAllergiesA, nullAllergiesB);
        assertEquals(nullFieldsA, nullFieldsB);
        assertEquals(nullFieldsA.hashCode(), nullFieldsB.hashCode());
        assertNotEquals(nullAllergiesA, base);
        assertNotEquals(base, nullAllergiesA);

        assertNotEquals(base, null);
        assertNotEquals(base, "not-a-user");
        assertNotEquals(base, new User(2L, "Pepe", 20, allergies));
        assertNotEquals(base, new User(1L, "Paco", 20, allergies));
        assertNotEquals(base, new User(1L, "Pepe", 21, allergies));
        assertNotEquals(base, new User(1L, "Pepe", 20, List.of(new Allergy("Dust", 1))));
        assertNotEquals(base, new User(null, "Pepe", 20, allergies));
        assertNotEquals(base, new User(1L, null, 20, allergies));
        assertNotEquals(new User(1L, null, 20, allergies), base);
    }

    @Test
    void equalsWithSubclassSameState() {
        User base = new User(1L, "Pepe", 20, List.of(new Allergy("Pollen", 2)));
        User child = new UserChild(1L, "Pepe", 20, List.of(new Allergy("Pollen", 2)));

        assertEquals(base, child);
        assertEquals(child, base);
        assertEquals(base.hashCode(), child.hashCode());
    }

    @Test
    void equalsWithSubclassCanEqualFalse() {
        User base = new User(1L, "Pepe", 20, List.of(new Allergy("Pollen", 2)));
        User strict = new UserChildStrict(1L, "Pepe", 20, List.of(new Allergy("Pollen", 2)));

        assertFalse(base.equals(strict));
        assertTrue(strict.equals(base));
    }

    @Test
    void toStringContainsUsefulFields() {
        User user = new User(1L, "Pepe", 20, null);
        String text = user.toString();

        assertTrue(text.contains("User"));
        assertTrue(text.contains("Pepe"));
        assertTrue(text.contains("20"));
    }
}
