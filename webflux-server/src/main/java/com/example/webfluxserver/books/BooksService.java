package com.example.webfluxserver.books;


import com.example.webfluxserver.exceptions.DataSaveException;
import com.example.webfluxserver.exceptions.BookNotFoundException;
import com.example.webfluxserver.exceptions.LiveEventNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BooksService {

    private final BooksRepository repository;

    public Flux<Book> getAll() {
        return repository
                .findAll()
                .switchIfEmpty(Mono.error(new BookNotFoundException("There are no books in the system currently")));
    }

    public Mono<Book> getById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new BookNotFoundException("No book was found with id: " + id)));
    }

    public Mono<Book> getByName(String name) {
        return repository
                .findByName(name)
                .switchIfEmpty(Mono.error(new BookNotFoundException("No book was found with name: " + name)));
    }

    public Mono<Book> insert(Book book) {
        return repository.save(book)
                .switchIfEmpty(Mono.error(new DataSaveException("Unable to save the book")));
    }

    public Flux<BookEvent> liveEvents(Book book) {
        return Flux.interval(Duration.ofSeconds(2))
                .take(5)
                .map(val -> new BookEvent(book, new Date(), getUser()))
                .switchIfEmpty(Mono.error(new LiveEventNotFoundException("There are no live events for the book - " + book.getName())));
    }

    private String getUser() {
        String[] names = {"John", "Marcus", "Susan", "Henry"};
        return names[new Random().nextInt(4)];
    }

}
