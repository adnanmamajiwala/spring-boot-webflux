package com.example.webfluxserver;

import com.example.webfluxserver.books.Book;
import com.example.webfluxserver.books.BooksRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;

@Configuration
@Slf4j
public class DataInitializer {

    @Bean
    public CommandLineRunner run(BooksRepository repository) {
        return args -> repository
                .count()
                .subscribe(val -> {
                    if (val == 0) {
                        repository.saveAll(Arrays.asList(getBooks()))
                                .subscribe(book -> log.debug(book.toString()));
                    }
                });
    }

    private Book[] getBooks() {
        return Try
                .of(() -> new ObjectMapper().readValue(new ClassPathResource("data.json").getInputStream(), Book[].class))
                .get();
    }
}
