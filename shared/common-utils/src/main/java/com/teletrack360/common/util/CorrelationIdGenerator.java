package com.teletrack360.common.util;

import java.util.UUID;

/**
 * Utility for generating correlation IDs for request tracing
 */
public class CorrelationIdGenerator {
    
    private CorrelationIdGenerator() {
        // Utility class
    }
    
    public static String generate() {
        return UUID.randomUUID().toString();
    }
    
    public static String generateWithPrefix(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString();
    }
}
