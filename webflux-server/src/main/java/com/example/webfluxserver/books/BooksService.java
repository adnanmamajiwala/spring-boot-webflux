package com.example.webfluxserver.books;


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
        return repository.findAll();
    }

    public Mono<Book> getById(String id) {
        return repository.findById(id);
    }

    public Mono<Book> getByName(String name) {
        return repository
                .findByName(name)
                .switchIfEmpty(Mono.error(new NoBookFoundException("No book was found with name: " + name)));
    }

    public Mono<Book> insert(Book book) {
        return repository.save(book);
    }

    public Flux<BookEvent> liveEvents(Book book) {
        return Flux.interval(Duration.ofSeconds(2))
                .take(5)
                .map(val -> new BookEvent(book, new Date(), getUser()))
                .log();
    }

    private String getUser() {
        String[] names = {"John", "Marcus", "Susan", "Henry"};
        return names[new Random().nextInt(4)];
    }

}
