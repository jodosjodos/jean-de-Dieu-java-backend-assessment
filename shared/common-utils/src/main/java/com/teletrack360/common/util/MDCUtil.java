package com.teletrack360.common.util;

import org.slf4j.MDC;

/**
 * Utility for managing MDC (Mapped Diagnostic Context) for correlation IDs
 */
public class MDCUtil {
    
    public static final String CORRELATION_ID_KEY = "correlationId";
    public static final String USER_ID_KEY = "userId";
    public static final String USERNAME_KEY = "username";
    
    private MDCUtil() {
        // Utility class
    }
    
    public static void setCorrelationId(String correlationId) {
        MDC.put(CORRELATION_ID_KEY, correlationId);
    }
    
    public static String getCorrelationId() {
        return MDC.get(CORRELATION_ID_KEY);
    }
    
    public static void setUserId(String userId) {
        MDC.put(USER_ID_KEY, userId);
    }
    
    public static void setUsername(String username) {
        MDC.put(USERNAME_KEY, username);
    }
    
    public static void clear() {
        MDC.clear();
    }
    
    public static void clearCorrelationId() {
        MDC.remove(CORRELATION_ID_KEY);
    }
}
