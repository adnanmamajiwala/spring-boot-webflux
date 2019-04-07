package com.example.webfluxserver.books;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String author;

    @NotBlank
    private String category;

}
