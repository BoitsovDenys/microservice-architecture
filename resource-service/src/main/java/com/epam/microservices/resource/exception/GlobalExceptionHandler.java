package com.epam.microservices.resource.exception;

import com.epam.microservices.resource.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), "404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidMP3FileException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMP3FileException(InvalidMP3FileException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), "400");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    @ExceptionHandler(ResourceServiceException.class)
    public ResponseEntity<ErrorResponse> handleResourceServiceException(ResourceServiceException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), "500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<ErrorResponse> handleEmptyFileException(EmptyFileException e) {
        Map<String, String> details = new HashMap<>();
        details.put("file", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Validation error", "400", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), "400");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeException(org.springframework.web.HttpMediaTypeNotSupportedException e) {
        ErrorResponse errorResponse = new ErrorResponse("Unsupported media type, request Content-Type is not supported", "415");
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    @ExceptionHandler(SongServiceException.class)
    public ResponseEntity<ErrorResponse> handleSongServiceException(SongServiceException e) {
        ErrorResponse errorResponse = new ErrorResponse("Song service error", "502");
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> fieldsWithErrors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            fieldsWithErrors.put(error.getField(), error.getDefaultMessage());
        }
        
        ErrorResponse errorResponse = new ErrorResponse("Validation error", "400", fieldsWithErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String value = String.valueOf(ex.getValue());
        Class<?> requiredType = ex.getRequiredType();

        String requirement;
        if (requiredType != null) {
            if (requiredType == Long.class || requiredType == long.class ||
                requiredType == Integer.class || requiredType == int.class ||
                requiredType == Short.class || requiredType == short.class ||
                requiredType == java.math.BigInteger.class) {
                requirement = "a whole number";
            } else if (requiredType == Double.class || requiredType == double.class ||
                       requiredType == Float.class || requiredType == float.class ||
                       requiredType == java.math.BigDecimal.class) {
                requirement = "a number";
            } else if (requiredType == Boolean.class || requiredType == boolean.class) {
                requirement = "either 'true' or 'false'";
            } else if ("UUID".equals(requiredType.getSimpleName())) {
                requirement = "a valid UUID";
            } else if ("LocalDate".equals(requiredType.getSimpleName())) {
                requirement = "a valid date";
            } else {
                requirement = "a valid value";
            }
        } else {
            requirement = "a valid value";
        }

        return new ErrorResponse(String.format("Invalid value '%s'. Must be %s.", value, requirement), "400");
    }
    
    @ExceptionHandler(ResourceDeletionException.class)
    public ResponseEntity<ErrorResponse> handleResourceDeletionException(ResourceDeletionException e) {
        ErrorResponse errorResponse = new ErrorResponse("Failed to delete one or more resources", "500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse("Internal server error", "500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
