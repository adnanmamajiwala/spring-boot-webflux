package com.example.webfluxserver.books;

import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class BooksControllerTest {

    private BooksController controller;
    private BooksService bookService = mock(BooksService.class);

    @Before
    public void setUp() throws Exception {
        controller = new BooksController(bookService);
    }

    @Test
    public void all_returnsAFluxOfBooks() {
        Book book1 = new Book("id1", "book1", "author1", "category1");;
        Book book2 = new Book("id2", "book2", "author2", "category2");
        given(bookService.getAll()).willReturn(Flux.just(book1, book2));

        StepVerifier
                .create(controller.all())
                .expectNext(book1)
                .expectNext(book2)
                .expectComplete()
                .verify();

        verify(bookService, times(1)).getAll();
    }

    @Test
    public void byId_returnsAMonoOfBook() {
        String id = "id1";
        Book book1 = new Book(id, "book1", "author1", "category1");;
        given(bookService.getById(id)).willReturn(Mono.just(book1));

        StepVerifier
                .create(controller.byId(id))
                .expectNext(book1)
                .expectComplete()
                .verify();

        verify(bookService, times(1)).getById(id);
    }

    @Test
    public void events_returnsAFluxOfBookEvents() {
        String id = "id1";
        Book book1 = new Book(id, "book1", "author1", "category1");;
        BookEvent bookEvent = new BookEvent(book1, new Date(), "SomeUser");
        given(bookService.getById(id)).willReturn(Mono.just(book1));
        given(bookService.liveEvents(book1)).willReturn(Flux.just(bookEvent));

        StepVerifier
                .create(controller.events(id))
                .expectNext(bookEvent)
                .expectComplete()
                .verify();

        verify(bookService, times(1)).getById(id);
        verify(bookService, times(1)).liveEvents(book1);
    }
}