package com.nyt.technews.config;

import com.nyt.technews.exception.FeedProcessingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientException(WebClientResponseException ex) {
        return ResponseEntity.status(ex.getStatusCode())
            .body("Error fetching data from NYT: " + ex.getMessage());
    }

    @ExceptionHandler(FeedProcessingException.class)
    public ResponseEntity<String> handleFeedProcessingException(FeedProcessingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error processing feed: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Internal server error: " + ex.getMessage());
    }
}
