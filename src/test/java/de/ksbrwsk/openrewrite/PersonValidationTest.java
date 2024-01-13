package de.ksbrwsk.openrewrite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PersonValidationTest {

    @Autowired
    Validator validator;

    @Test
    void nameShouldNotBeBlank() {
        Person person = new Person("");
        var violations = validator.validate(person);
        assertThat(violations.size()).isEqualTo(2);
    }

    @Test
    void nameShouldNotBeLongerThan10() {
        Person person = new Person("12345678901");
        var violations = validator.validate(person);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void nameShouldNotBeShorterThan1() {
        Person person = new Person("");
        var violations = validator.validate(person);
        assertThat(violations.size()).isEqualTo(2);
    }

    @Test
    void nameShouldBeValid() {
        Person person = new Person("John Doe");
        var violations = validator.validate(person);
        assertThat(violations.size()).isEqualTo(0);
    }

}