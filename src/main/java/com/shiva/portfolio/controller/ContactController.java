package com.shiva.portfolio.controller;

import com.shiva.portfolio.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/public/contact")
@RequiredArgsConstructor
public class ContactController {

    private final EmailService emailService;
    
    private final ConcurrentHashMap<String, Instant> rateLimiter = new ConcurrentHashMap<>();
    
    private static final long RATE_LIMIT_MINUTES = 10;

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody ContactRequest request, HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getRemoteAddr();
        
        // Rate Limiting Check
        Instant lastRequest = rateLimiter.get(clientIp);
        if (lastRequest != null && Duration.between(lastRequest, Instant.now()).toMinutes() < RATE_LIMIT_MINUTES) {
            long waitMinutes = RATE_LIMIT_MINUTES - Duration.between(lastRequest, Instant.now()).toMinutes();
            // Fallback for cases where it's < 1 minute
            if (waitMinutes == 0) waitMinutes = 1; 
            
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Map.of("error", "You have already sent a message recently. Please wait " + waitMinutes + " minutes before sending another."));
        }

        if (request.getName() == null || request.getName().isBlank() ||
            request.getEmail() == null || request.getEmail().isBlank() ||
            request.getMessage() == null || request.getMessage().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "All fields are required."));
        }

        try {
            emailService.sendContactEmail(
                request.getName().trim(),
                request.getEmail().trim(),
                request.getMessage().trim()
            );
            
            // Record the successful email send in the rate limiter
            rateLimiter.put(clientIp, Instant.now());
            
            return ResponseEntity.ok(Map.of("success", true, "message", "Message sent successfully!"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to send message. Please try again later."));
        }
    }

    @Data
    public static class ContactRequest {
        private String name;
        private String email;
        private String message;
    }
}

