package com.example.webfluxserver.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionsAdvice {

    @ExceptionHandler(value = BookNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorResponse handleError(BookNotFoundException ex) {
        log.error("Error in trying to find book ", ex);
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.toString())
                .build();
    }

    @ExceptionHandler(value = LiveEventNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorResponse handleError(LiveEventNotFoundException ex) {
        log.error("Error in trying to find event for book ", ex);
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.toString())
                .build();
    }

    @ExceptionHandler(value = DataSaveException.class)
    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public ErrorResponse handleError(DataSaveException ex) {
        log.error("Error in trying to save data ", ex);
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.EXPECTATION_FAILED.toString())
                .build();
    }

}
