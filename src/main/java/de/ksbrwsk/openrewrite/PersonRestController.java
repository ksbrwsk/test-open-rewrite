package de.ksbrwsk.openrewrite;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(PersonRestController.API)
@RequiredArgsConstructor
public class PersonRestController {
    public static final String API = "/api/people";

    private final PersonRepository personRepository;

    @GetMapping
    public ResponseEntity<Iterable<Person>> handleFindAll() {
        Iterable<Person> all = personRepository.findAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Person> handleFindById(@NotNull @PathVariable Long id) {
        Optional<Person> byId = personRepository.findById(id);
        return ResponseEntity.of(byId);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> handleDeleteById(@NotNull @PathVariable Long id) {
        Optional<Person> optionalPerson = this.personRepository.findById(id);
        if (optionalPerson.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            this.personRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Person> handleUpdateById(@NotNull @PathVariable Long id,
                                                   @NotNull @RequestBody Person person) {
        Optional<Person> optionalPerson = this.personRepository.findById(id);
        if (optionalPerson.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Person personToUpdate = optionalPerson.get();
            Person saved = this.personRepository.save(new Person(id, personToUpdate.name()));
            return ResponseEntity.ok(saved);
        }
    }

    @PostMapping
    public ResponseEntity<Person> handleCreate(@NotNull @RequestBody Person person) {
        Person saved = this.personRepository.save(new Person(person.name()));
        return ResponseEntity.created(URI.create(API + "/" + saved.id())).body(saved);
    }
}
