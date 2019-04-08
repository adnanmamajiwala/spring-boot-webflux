package com.example.webfluxserver.books;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebFluxTest(BooksController.class)
public class BooksControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BooksService booksService;
    private Book expected;

    @Before
    public void setUp() throws Exception {
        expected = new Book(null, "Test1", "Author1", "SomeCategory");
    }

    @Test
    public void all_returnsAListOfAllBooks() {
        Book expected2 = new Book(null, "Test2", "Author2", "SomeOtherCategory");

        given(booksService.getAll()).willReturn(Flux.just(expected, expected2));

        webTestClient.get()
                .uri("/books")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectBodyList(Book.class)
                .hasSize(2)
                .contains(expected, expected2);
    }

    @Test
    public void byId_returnsBookThatMatchesTheId() {
        given(booksService.getById(anyString())).willReturn(Mono.just(expected));

        webTestClient.get()
                .uri("/books/someId")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectBody(Book.class)
                .isEqualTo(expected);
    }

    @Test
    public void byName_returnsBookThatMatchesTheName() {
        given(booksService.getByName("Test1")).willReturn(Mono.just(expected));

        webTestClient.get()
                .uri("/books/search?name=Test1")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectBody(Book.class)
                .isEqualTo(expected);
    }


}