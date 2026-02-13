package com.teletrack360.common.exception;

import lombok.Getter;

import java.util.Map;

/**
 * Exception thrown for validation failures
 */
@Getter
public class ValidationException extends BusinessException {
    
    private final Map<String, String> fieldErrors;
    
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
        this.fieldErrors = null;
    }
    
    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message, "VALIDATION_ERROR");
        this.fieldErrors = fieldErrors;
    }
}
