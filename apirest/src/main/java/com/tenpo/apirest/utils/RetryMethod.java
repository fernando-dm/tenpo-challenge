package com.tenpo.apirest.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class RetryMethod {
    // punto a.iv: Plus agrego un tiempo de espera para no reintentar enseguida
    public static <T> T retryMethod(int maxRetries, String functionName, Supplier<T> methodCall) throws Exception {
        int retries = 0;
//        Exception lastException = null;
        Exception lastException = new RuntimeException("Error occurred during method call");

        while (retries < maxRetries) {
            try {
                return methodCall.get();
            } catch (Exception e) {
                lastException = e;
                log.error("Error calling " + functionName + " on attempt " + retries + " message: " + e.getMessage());
                retries++;
                Thread.sleep(3000L * retries);  // no reintento de una, sino que espero
            }
        }
        log.error("Failed to call " + functionName + " attempts: " + maxRetries + " .Last exception message: " + lastException.getMessage());
        throw lastException;
    }
}