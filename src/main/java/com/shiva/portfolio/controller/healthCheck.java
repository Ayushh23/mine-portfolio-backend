package com.shiva.portfolio.controller;

import com.shiva.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class healthCheck {

    private final PortfolioService portfolioService;

    /**
     * Warmup endpoint used by UptimeRobot to keep Render free-tier alive.
     *
     * UptimeRobot sends HEAD requests by default, so we handle both HEAD and GET.
     * On every ping, we call PortfolioService which touches ALL MongoDB repositories
     * (projects, skills, experiences, testimonials, site settings), forcing a full
     * JVM + DB connection warm-up so subsequent real user requests are fast.
     */
    @RequestMapping(value = "/health", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ResponseEntity<?> warmup() {
        try {
            // Touch every MongoDB collection — warms up JVM + DB connection pool
            portfolioService.getPublicPortfolioData();

            return ResponseEntity.ok(Map.of(
                "status", "ok",
                "warmedUp", true,
                "timestamp", Instant.now().toString()
            ));
        } catch (Exception e) {
            // Still return 200 so UptimeRobot doesn't alert on a DB hiccup,
            // but flag that warm-up partially failed for debugging.
            return ResponseEntity.ok(Map.of(
                "status", "ok",
                "warmedUp", false,
                "error", e.getMessage(),
                "timestamp", Instant.now().toString()
            ));
        }
    }
}

