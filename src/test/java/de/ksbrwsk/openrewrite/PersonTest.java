package de.ksbrwsk.openrewrite;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PersonTest {

    @Test
    void create() {
        Person person = new Person(1L, "John Doe");
        assertThat(person.name()).isEqualTo("John Doe");
        assertThat(person.id()).isEqualTo(1L);
    }
}