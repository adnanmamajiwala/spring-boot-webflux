package com.example.webfluxclient;

import com.example.webfluxclient.books.Book;
import com.example.webfluxclient.books.BookEvent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class WebfluxClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxClientApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        WebClient client = WebClient.create();
        return args -> client.get()
                .uri("http://localhost:8090/books")
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Book.class))
                .take(1)
                .switchMap(book -> client.get().uri("http://localhost:8090/books/{id}/events", book.getId()).exchange())
                .flatMap(clientResponse -> clientResponse.bodyToFlux(BookEvent.class))
                .subscribe(System.out::println);
    }

}
