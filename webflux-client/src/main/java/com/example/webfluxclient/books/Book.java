package com.example.webfluxclient.books;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String author;

    @NotBlank
    private String category;

}
