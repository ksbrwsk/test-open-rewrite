package de.ksbrwsk.openrewrite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.Validator;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PersonValidationTest {

    @Autowired
    Validator validator;

    @Test
    void nameShouldNotBeBlank() {
        Person person = new Person("");
        var violations = validator.validate(person);
        assertEquals(violations.size(), 2);
    }

    @Test
    void nameShouldNotBeLongerThan10() {
        Person person = new Person("12345678901");
        var violations = validator.validate(person);
        assertEquals(violations.size(), 1);
    }

    @Test
    void nameShouldNotBeShorterThan1() {
        Person person = new Person("");
        var violations = validator.validate(person);
        assertEquals(violations.size(), 2);
    }

    @Test
    void nameShouldBeValid() {
        Person person = new Person("John Doe");
        var violations = validator.validate(person);
        assertEquals(violations.size(), 0);
    }

}