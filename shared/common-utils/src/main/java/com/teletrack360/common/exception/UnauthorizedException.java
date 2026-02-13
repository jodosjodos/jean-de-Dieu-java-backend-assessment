package com.teletrack360.common.exception;

/**
 * Exception thrown for unauthorized access attempts
 */
public class UnauthorizedException extends BusinessException {
    
    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED");
    }
    
    public UnauthorizedException() {
        super("Access denied. Authentication required.", "UNAUTHORIZED");
    }
}
