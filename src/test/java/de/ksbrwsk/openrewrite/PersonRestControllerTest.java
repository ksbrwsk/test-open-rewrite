package de.ksbrwsk.openrewrite;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Optional;

import static de.ksbrwsk.openrewrite.PersonRestController.API;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebFluxTest
@Import(PersonRestController.class)
class PersonRestControllerTest {

    private static final String API_ONE = "/api/people/1";

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    PersonRepository personRepository;

    @Test
    void handleNotFound() {
        this.webTestClient
                .get()
                .uri("/api/pepl")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void handleFindAll() {
        when(this.personRepository.findAll()).thenReturn(
                List.of(new Person(1L, "John Doe"), new Person(2L, "Jane Doe"))
        );
        this.webTestClient
                .get()
                .uri(API)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("John Doe")
                .jsonPath("$[1].name").isEqualTo("Jane Doe");
    }

    @Test
    void handleFindById() {
        when(this.personRepository.findById(Mockito.anyLong())).thenReturn(
                Optional.of(new Person(1L, "John Doe"))
        );
        this.webTestClient
                .get()
                .uri(API_ONE)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("John Doe");
    }

    @Test
    void handleFindByIdNotFound() {
        when(this.personRepository.findById(Mockito.anyLong())).thenReturn(
                Optional.empty()
        );
        this.webTestClient
                .get()
                .uri(API_ONE)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void handleDeleteById() {
        when(this.personRepository.findById(Mockito.anyLong())).thenReturn(
                Optional.of(new Person(1L, "John Doe"))
        );
        doNothing().when(this.personRepository).delete(any(Person.class));

        this.webTestClient
                .delete()
                .uri(API_ONE)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void handleDeleteByIdNotFound() {
        when(this.personRepository.findById(Mockito.anyLong())).thenReturn(
                Optional.empty()
        );
        this.webTestClient
                .delete()
                .uri(API_ONE)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void handleUpdateById() {
        when(this.personRepository.findById(Mockito.anyLong())).thenReturn(
                Optional.of(new Person(1L, "John Doe"))
        );
        when(this.personRepository.save(any(Person.class))).thenReturn(
                new Person(1L, "Jane Doe")
        );
        this.webTestClient
                .put()
                .uri(API_ONE)
                .bodyValue(new Person(1L, "Jane Doe"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Jane Doe");
    }

    @Test
    void handleUpdateByIdNotFound() {
        when(this.personRepository.findById(Mockito.anyLong())).thenReturn(
                Optional.empty()
        );
        this.webTestClient
                .put()
                .uri(API_ONE)
                .bodyValue(new Person(1L, "Jane Doe"))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void handleUpdateByIdBadRequest() {
        this.webTestClient
                .put()
                .uri(API_ONE)
                .bodyValue(Optional.empty())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void handleCreate() {
        when(this.personRepository.save(any(Person.class))).thenReturn(
                new Person(1L, "John Doe")
        );
        this.webTestClient
                .post()
                .uri(API)
                .bodyValue(new Person(1L, "John Doe"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader()
                .location(API_ONE)
                .expectBody()
                .jsonPath("$.id").isEqualTo(1L)
                .jsonPath("$.name").isEqualTo("John Doe");
    }

    @Test
    void handleCreateBadRequest() {
        this.webTestClient
                .post()
                .uri(API)
                .bodyValue(Optional.empty())
                .exchange()
                .expectStatus().isBadRequest();
    }
}