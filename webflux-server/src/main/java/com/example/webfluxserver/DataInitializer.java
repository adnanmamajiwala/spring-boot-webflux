package com.example.webfluxserver;

import com.example.webfluxserver.books.Book;
import com.example.webfluxserver.books.BooksRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.stream.Stream;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner run(BooksRepository repository) {
        return args -> repository
                .count()
                .subscribe(val -> {
                    if (val == 0) {
                        Stream.of(getBooks())
                                .map(repository::save)
                                .forEach(bookMono -> bookMono.subscribe(System.out::println));
                    }
                });
    }

    private Book[] getBooks() {
        return Try
                .of(() -> new ObjectMapper().readValue(new ClassPathResource("data.json").getInputStream(), Book[].class))
                .get();
    }
}
