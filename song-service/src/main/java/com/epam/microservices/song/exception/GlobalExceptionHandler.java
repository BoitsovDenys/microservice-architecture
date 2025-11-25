package com.epam.microservices.song.exception;

import com.epam.microservices.song.dto.ErrorResponse;
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

    @ExceptionHandler(SongNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSongNotFoundException(SongNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), "404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
    @ExceptionHandler(SongAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleSongAlreadyExistsException(SongAlreadyExistsException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), "409");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), "400");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
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
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse("Internal server error", "500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
