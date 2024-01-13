package de.ksbrwsk.openrewrite;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record Person(@Id Long id, @NotBlank @Size(min = 1, max = 10) String name) {
    public Person(String name) {
        this(null, name);
    }
}
