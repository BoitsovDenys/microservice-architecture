package com.epam.microservices.resource.exception;

public class SongServiceException extends RuntimeException {
    public SongServiceException(String message) {
        super(message);
    }
}
