package com.epam.microservices.resource.exception;

public class ResourceServiceException extends RuntimeException {
    public ResourceServiceException(String message) {
        super(message);
    }
}
