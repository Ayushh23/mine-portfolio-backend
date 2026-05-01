package com.shiva.portfolio.config;

import com.shiva.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheWarmupRunner {

    private final PortfolioService portfolioService;

    /**
     * Fires immediately after the application is fully started and ready to serve traffic.
     * Pre-populates the "portfolioData" cache by calling the service once.
     *
     * Result: every visitor — including the very first one — gets data served
     * from RAM with no MongoDB round-trip at all.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void warmUpCache() {
        log.info(">>> Cache warm-up starting...");
        try {
            portfolioService.getPublicPortfolioData();
            log.info(">>> Cache warm-up complete. All portfolio data is pre-loaded.");
        } catch (Exception e) {
            // Don't crash the app if MongoDB is temporarily unreachable at startup
            log.warn(">>> Cache warm-up failed (MongoDB may not be ready yet): {}", e.getMessage());
        }
    }
}
