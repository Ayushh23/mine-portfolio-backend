package com.shiva.portfolio.service;

import com.shiva.portfolio.repository.ProjectRepository;
import com.shiva.portfolio.repository.SkillRepository;
import com.shiva.portfolio.repository.ExperienceRepository;
import com.shiva.portfolio.repository.SiteSettingRepository;
import com.shiva.portfolio.model.SiteSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final ExperienceRepository experienceRepository;
    private final com.shiva.portfolio.repository.TestimonialRepository testimonialRepository;
    private final SiteSettingRepository siteSettingRepository;

    public Map<String, Object> getPublicPortfolioData() {
        Map<String, Object> data = new HashMap<>();
        data.put("projects", projectRepository.findAll());
        data.put("skills", skillRepository.findAll());
        data.put("experiences", experienceRepository.findAll());
        data.put("testimonials", testimonialRepository.findAll());
        
        // Convert settings list to a flat map
        Map<String, String> settingsMap = new HashMap<>();
        for (SiteSetting setting : siteSettingRepository.findAll()) {
            settingsMap.put(setting.getKey(), setting.getValue());
        }
        data.put("settings", settingsMap);
        
        return data;
    }
}
