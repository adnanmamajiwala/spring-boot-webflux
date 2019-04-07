package com.example.webfluxserver.books;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookEvent {

    private Book book;
    private Date readOn;
    private String byUser;

}
