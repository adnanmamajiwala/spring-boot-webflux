package com.example.webfluxserver.books;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BooksRepository extends ReactiveMongoRepository<Book, String> {

    Mono<Book> findByName(String name);

}
