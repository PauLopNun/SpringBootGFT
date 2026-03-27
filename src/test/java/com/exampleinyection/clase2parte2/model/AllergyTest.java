package com.exampleinyection.clase2parte2.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AllergyTest {

    private static class AllergyChild extends Allergy {
        AllergyChild(String name, int severity) {
            super(name, severity);
        }
    }

    private static class AllergyChildStrict extends Allergy {
        AllergyChildStrict(String name, int severity) {
            super(name, severity);
        }

        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }

    @Test
    void gettersAndSetters() {
        Allergy allergy = new Allergy("Pollen", 5);

        assertEquals("Pollen", allergy.getName());
        assertEquals(5, allergy.getSeverity());

        allergy.setName("Dust");
        allergy.setSeverity(2);

        assertEquals("Dust", allergy.getName());
        assertEquals(2, allergy.getSeverity());
    }

    @Test
    void equalsAndHashCodeCoverBranches() {
        Allergy base = new Allergy("Dust", 2);
        Allergy sameValues = new Allergy("Dust", 2);
        Allergy nullNameA = new Allergy(null, 2);
        Allergy nullNameB = new Allergy(null, 2);

        assertEquals(base, base);
        assertEquals(base, sameValues);
        assertEquals(base.hashCode(), sameValues.hashCode());
        assertEquals(nullNameA, nullNameB);
        assertEquals(nullNameA.hashCode(), nullNameB.hashCode());

        assertNotEquals(base, null);
        assertNotEquals(base, "not-an-allergy");
        assertNotEquals(base, new Allergy("Pollen", 2));
        assertNotEquals(base, new Allergy("Dust", 3));
        assertNotEquals(base, nullNameA);
        assertNotEquals(nullNameA, base);
    }

    @Test
    void equalsWithSubclassSameState() {
        Allergy base = new Allergy("Dust", 2);
        Allergy child = new AllergyChild("Dust", 2);

        assertEquals(base, child);
        assertEquals(child, base);
        assertEquals(base.hashCode(), child.hashCode());
    }

    @Test
    void equalsWithSubclassCanEqualFalse() {
        Allergy base = new Allergy("Dust", 2);
        Allergy strict = new AllergyChildStrict("Dust", 2);

        assertFalse(base.equals(strict));
        assertTrue(strict.equals(base));
    }

    @Test
    void toStringContainsUsefulFields() {
        Allergy allergy = new Allergy("Dust", 2);
        String text = allergy.toString();

        assertTrue(text.contains("Allergy"));
        assertTrue(text.contains("Dust"));
        assertTrue(text.contains("2"));
    }
}
