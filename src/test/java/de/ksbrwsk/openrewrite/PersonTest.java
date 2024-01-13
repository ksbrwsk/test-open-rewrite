package de.ksbrwsk.openrewrite;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonTest {

    @Test
    void create() {
        Person person = new Person(1L, "John Doe");
        assertEquals(person.name(), "John Doe");
        assertEquals(person.id(), 1L);
    }
}