package com.shiva.portfolio.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    /**
     * Configures a Caffeine-backed cache with a 10-minute TTL (expireAfterWrite).
     *
     * This means:
     *  - Data is served from RAM for up to 10 minutes (fast responses).
     *  - After 10 minutes, the cache auto-expires and the next request fetches
     *    fresh data from MongoDB — so the resume URL (and any other settings)
     *    are guaranteed to be at most 10 minutes stale.
     *  - Admin changes (POST/PUT/DELETE) trigger an immediate @CacheEvict,
     *    so updates appear instantly without waiting for the TTL.
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager("portfolioData");
        manager.setCaffeine(
            Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)  // auto-refresh every 10 min
                .maximumSize(100)
        );
        return manager;
    }
}
