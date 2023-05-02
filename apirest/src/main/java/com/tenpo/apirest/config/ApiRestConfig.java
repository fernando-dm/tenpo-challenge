package com.tenpo.apirest.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.util.function.Function;

@Configuration
@EnableCaching
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ApiRestConfig {

    //punto c, soporta 3 rpm y
    @Bean
    public RateLimiter rateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .limitForPeriod(3)
                .timeoutDuration(Duration.ofSeconds(1))
                .build();
        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        return registry.rateLimiter("rateLimiter");
    }

    @Bean
    public CustomFallback customFallback() {
        return new CustomFallback();
    }

    public static class CustomFallback implements Function<Throwable, ResponseEntity<?>> {
        @Override
        public ResponseEntity<?> apply(Throwable throwable) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many requests. Please try again later.");
        }
    }

}
