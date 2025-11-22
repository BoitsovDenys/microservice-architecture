package com.epam.microservices.resource.exception;

public class ResourceDeletionException extends RuntimeException {
    public ResourceDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
