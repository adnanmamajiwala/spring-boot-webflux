package com.example.webfluxserver.books;

import com.example.webfluxserver.WebfluxServerApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = WebfluxServerApplication.class)
public class BooksRepositoryTest {

    @Autowired
    private BooksRepository repository;
    private Book book1;
    private Book book2;

    @Before
    public void setUp() throws Exception {
        repository.deleteAll().block();
        book1 = new Book("1", "Book1", "Author1", "Category");
        book2 = new Book("2", "Hello", "Test", "Sample");
        repository.saveAll(Arrays.asList(book1, book2)).subscribe();
    }

    @Test
    public void findById_whenValidId_thenFindMatchingId() {
        Mono<Book> bookMono = repository.findById("1");

        StepVerifier
                .create(bookMono)
                .assertNext(book -> assertThat(book).isEqualToComparingFieldByField(book1))
                .expectComplete()
                .verify();
    }

    @Test
    public void save_whenNoIdGiven_thenCreatesNewDocumentWithId() {
        Book expected = new Book(null, "Test", "Auth", "Category");
        Mono<Book> bookMono = repository.save(expected);

        StepVerifier
                .create(bookMono)
                .assertNext(book -> {
                    assertThat(book.getId()).isNotNull();
                    assertThat(book.getName()).isEqualToIgnoringCase(expected.getName());
                    assertThat(book.getAuthor()).isEqualToIgnoringCase(expected.getAuthor());
                    assertThat(book.getCategory()).isEqualToIgnoringCase(expected.getCategory());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void findAll_returnAllBooksInFlux() {
        Flux<Book> bookFlux = repository.findAll();
        StepVerifier
                .create(bookFlux)
                .expectNext(book1)
                .assertNext(book -> {
                    assertThat(book.getId()).isEqualTo(book2.getId());
                    assertThat(book.getName()).isEqualToIgnoringCase(book2.getName());
                    assertThat(book.getAuthor()).isEqualToIgnoringCase(book2.getAuthor());
                    assertThat(book.getCategory()).isEqualToIgnoringCase(book2.getCategory());
                })
                .expectComplete()
                .verify();
    }
}