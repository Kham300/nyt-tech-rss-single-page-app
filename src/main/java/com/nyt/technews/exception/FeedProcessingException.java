package com.nyt.technews.exception;

public class FeedProcessingException extends RuntimeException {

    public FeedProcessingException(String errorParsingRssFeed, Throwable e) {
        super(errorParsingRssFeed, e);
    }
}
