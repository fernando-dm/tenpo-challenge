package com.tenpo.apirest.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RetryMethodShould {
    @Test
    void shouldRetryMethod() throws Exception {
        AtomicInteger counter = new AtomicInteger(0);
        String result = RetryMethod.retryMethod(3, "testMethod", () -> {
            if (counter.getAndIncrement() < 2) {
                throw new RuntimeException("Error occurred during method call");
            } else {
                return "success";
            }
        });
        assertEquals("success", result);
    }

    @Test
    void shouldRetryMethodAndThrowExceptionAfterMaxRetries() {
        Supplier<Object> failingMethod = () -> {
            throw new RuntimeException("Error occurred during method call");
        };

        Exception exception = assertThrows(Exception.class, () -> RetryMethod.retryMethod(3, "testMethod", failingMethod));
        assertEquals("Error occurred during method call", exception.getMessage());
    }

}
