package com.github.loks0n.marvel.exception;

import com.github.loks0n.marvel.exception.custom.MarvelCharacterNotFoundException;
import com.github.loks0n.marvel.exception.custom.MarvelClientConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.client.WebClientException;

@ControllerAdvice
public class MarvelExceptionHandler {

    @ExceptionHandler(WebClientException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ResponseBody
    ErrorResponse handle(WebClientException exception) {
        return new ErrorResponse("WebClientException");
    }

    @ExceptionHandler(MarvelClientConflictException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ResponseBody
    ErrorResponse handle(MarvelClientConflictException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(MarvelCharacterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    ErrorResponse handle(MarvelCharacterNotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }

}
