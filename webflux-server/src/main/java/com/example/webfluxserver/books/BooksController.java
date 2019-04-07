package com.example.webfluxserver.books;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {

    private final BooksService service;

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Book> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Book> byId(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping("/search")
    public Mono<Book> byName(@RequestParam("name") String name) {
        return service.getByName(name);
    }

    @PostMapping
    public Mono<Book> save(@Valid @RequestBody Book book) {
        return service.insert(book);
    }

    @GetMapping(value = "/{id}/events", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<BookEvent> events(@PathVariable String id) {
        return service.getById(id)
                .flatMapMany(service::eventStream);
    }

}
