package com.epam.microservices.resource.exception;

public class InvalidMP3FileException extends RuntimeException {
    public InvalidMP3FileException(String message) {
        super(message);
    }
}
