package com.webapplication.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public Mono<ServerResponse> handleTaskNotFoundException(TaskNotFoundException ex) {
        return ServerResponse.status(HttpStatus.NOT_FOUND)
                .bodyValue(new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(InvalidTaskException.class)
    public Mono<ServerResponse> handleInvalidTaskException(InvalidTaskException ex) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .bodyValue(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
}
