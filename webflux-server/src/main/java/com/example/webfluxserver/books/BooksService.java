package com.example.webfluxserver.books;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Date;
import java.util.Random;
import java.util.stream.Stream;

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
        return repository.findByName(name);
    }

    public Mono<Book> insert(Book book) {
        return repository.save(book);
    }

    public Flux<BookEvent> eventStream(Book book) {
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(2)).take(5);
        Flux<BookEvent> events = Flux.fromStream(Stream.generate(() -> new BookEvent(book, new Date(), getUser())));
        return Flux.zip(interval, events)
                .map(Tuple2::getT2);
    }

    private String getUser() {
        String[] names = {"John", "Marcus", "Susan", "Henry"};
        return names[new Random().nextInt(4)];
    }

}
