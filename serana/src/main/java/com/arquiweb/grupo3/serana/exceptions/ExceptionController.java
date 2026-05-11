/*package com.arquiweb.grupo3.serana.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {


    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ExceptionMessage resourceNotFoundException(ResourceNotFoundException e, WebRequest r) {
        return new ExceptionMessage(HttpStatus.NOT_FOUND.value(),
                e.getClass().getSimpleName(), e.getMessage(),
                r.getDescription(false), LocalDateTime.now());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public ExceptionMessage validationException(ValidationException e, WebRequest r) {
        return new ExceptionMessage(HttpStatus.NOT_ACCEPTABLE.value(),
                e.getClass().getSimpleName(), e.getMessage(),
                r.getDescription(false), LocalDateTime.now());
    }

}*/
