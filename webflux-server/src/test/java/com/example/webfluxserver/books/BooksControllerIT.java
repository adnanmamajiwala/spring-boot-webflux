package com.example.webfluxserver.books;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebFluxTest(BooksController.class)
public class BooksControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BooksService booksService;

    @Test
    public void all_returnsAListOfAllBooks() {
        Book book1 = new Book(null, "Test1", "Author1", "SomeCategory");
        Book book2 = new Book(null, "Test2", "Author2", "SomeOtherCategory");

        given(booksService.getAll()).willReturn(Flux.just(book1, book2));

        webTestClient.get()
                .uri("/books")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectBodyList(Book.class)
                .hasSize(2)
                .contains(book1, book2);
    }


}