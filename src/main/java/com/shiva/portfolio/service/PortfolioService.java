package com.shiva.portfolio.service;

import com.shiva.portfolio.repository.ProjectRepository;
import com.shiva.portfolio.repository.SkillRepository;
import com.shiva.portfolio.repository.ExperienceRepository;
import com.shiva.portfolio.repository.SiteSettingRepository;
import com.shiva.portfolio.repository.TestimonialRepository;
import com.shiva.portfolio.model.SiteSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final ExperienceRepository experienceRepository;
    private final TestimonialRepository testimonialRepository;
    private final SiteSettingRepository siteSettingRepository;

    /**
     * Fetches all portfolio data in parallel and caches the result in memory.
     * The cache is evicted whenever an admin makes a change (see AdminPortfolioController).
     * This means the first request fetches from MongoDB; every subsequent request
     * is served from RAM (~0ms) until data changes.
     */
    @Cacheable("portfolioData")
    public Map<String, Object> getPublicPortfolioData() {
        // Fire all 5 MongoDB queries in parallel
        CompletableFuture<Object> projectsFuture     = CompletableFuture.supplyAsync(() -> projectRepository.findAll());
        CompletableFuture<Object> skillsFuture       = CompletableFuture.supplyAsync(() -> skillRepository.findAll());
        CompletableFuture<Object> experiencesFuture  = CompletableFuture.supplyAsync(() -> experienceRepository.findAll());
        CompletableFuture<Object> testimonialsFuture = CompletableFuture.supplyAsync(() -> testimonialRepository.findAll());
        CompletableFuture<Object> settingsFuture     = CompletableFuture.supplyAsync(() -> {
            Map<String, String> settingsMap = new HashMap<>();
            for (SiteSetting s : siteSettingRepository.findAll()) {
                settingsMap.put(s.getKey(), s.getValue());
            }
            return settingsMap;
        });

        // Wait for all queries to complete
        CompletableFuture.allOf(projectsFuture, skillsFuture, experiencesFuture, testimonialsFuture, settingsFuture).join();

        Map<String, Object> data = new HashMap<>();
        data.put("projects",      projectsFuture.join());
        data.put("skills",        skillsFuture.join());
        data.put("experiences",   experiencesFuture.join());
        data.put("testimonials",  testimonialsFuture.join());
        data.put("settings",      settingsFuture.join());
        return data;
    }
}
