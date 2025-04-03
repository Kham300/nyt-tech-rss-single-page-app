package com.nyt.technews.exception;

public class FeedProcessingException extends RuntimeException {

    public FeedProcessingException(String errorParsingRssFeed, Exception e) {
        super(errorParsingRssFeed, e);
    }
}
