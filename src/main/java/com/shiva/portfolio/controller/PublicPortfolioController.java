package com.shiva.portfolio.controller;

import com.shiva.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicPortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/data")
    public ResponseEntity<?> getPortfolioData() {
        return ResponseEntity.ok(portfolioService.getPublicPortfolioData());
    }
}
