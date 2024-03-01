package com.np.elasticsearch.query.service.common.api.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ElasticsearchQueryServiceErrorHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handle(AccessDeniedException e) {
        log.warn("Access denied exception", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You're not authorized to access this resource!");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handle(IllegalArgumentException e) {
        log.warn("Illegal argument exception", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                String.format("Illegal argument exception! %s", e.getMessage())
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException e) {
        log.error("Runtime exception", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                String.format("Internal server error! %s", e.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handle(MethodArgumentNotValidException e) {
        log.warn("Method argument not valid exception", e);
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(
                error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()
                ));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
