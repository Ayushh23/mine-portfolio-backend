package com.shiva.portfolio.controller;

import com.shiva.portfolio.model.Experience;
import com.shiva.portfolio.model.Project;
import com.shiva.portfolio.model.Skill;
import com.shiva.portfolio.model.Testimonial;
import com.shiva.portfolio.repository.ExperienceRepository;
import com.shiva.portfolio.repository.ProjectRepository;
import com.shiva.portfolio.repository.SkillRepository;
import com.shiva.portfolio.model.*;
import com.shiva.portfolio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminPortfolioController {

    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final ExperienceRepository experienceRepository;
    private final TestimonialRepository testimonialRepository;
    private final SiteSettingRepository siteSettingRepository;

    // --- Projects ---
    @PostMapping("/projects")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectRepository.save(project));
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable String id, @RequestBody Project project) {
        project.setId(id);
        return ResponseEntity.ok(projectRepository.save(project));
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        projectRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // --- Skills ---
    @PostMapping("/skills")
    public ResponseEntity<Skill> createSkill(@RequestBody Skill skill) {
        return ResponseEntity.ok(skillRepository.save(skill));
    }

    @PutMapping("/skills/{id}")
    public ResponseEntity<Skill> updateSkill(@PathVariable String id, @RequestBody Skill skill) {
        skill.setId(id);
        return ResponseEntity.ok(skillRepository.save(skill));
    }

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable String id) {
        skillRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // --- Experiences ---
    @PostMapping("/experiences")
    public ResponseEntity<Experience> createExperience(@RequestBody Experience experience) {
        return ResponseEntity.ok(experienceRepository.save(experience));
    }

    @PutMapping("/experiences/{id}")
    public ResponseEntity<Experience> updateExperience(@PathVariable String id, @RequestBody Experience experience) {
        experience.setId(id);
        return ResponseEntity.ok(experienceRepository.save(experience));
    }

    @DeleteMapping("/experiences/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable String id) {
        experienceRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // --- Testimonials ---
    @PostMapping("/testimonials")
    public ResponseEntity<Testimonial> createTestimonial(@RequestBody Testimonial testimonial) {
        return ResponseEntity.ok(testimonialRepository.save(testimonial));
    }

    @PutMapping("/testimonials/{id}")
    public ResponseEntity<Testimonial> updateTestimonial(@PathVariable String id, @RequestBody Testimonial testimonial) {
        testimonial.setId(id);
        return ResponseEntity.ok(testimonialRepository.save(testimonial));
    }

    @DeleteMapping("/testimonials/{id}")
    public ResponseEntity<Void> deleteTestimonial(@PathVariable String id) {
        testimonialRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // ---- Site Settings ----
    @GetMapping("/settings")
    public List<SiteSetting> getSettings() {
        return siteSettingRepository.findAll();
    }

    @PutMapping("/settings")
    public ResponseEntity<SiteSetting> updateSetting(@RequestBody Map<String, String> payload) {
        String key = payload.get("key");
        String value = payload.get("value");
        if (key == null) return ResponseEntity.badRequest().build();

        SiteSetting setting = siteSettingRepository.findByKey(key)
                .orElse(new SiteSetting(key, value));
        setting.setValue(value);
        return ResponseEntity.ok(siteSettingRepository.save(setting));
    }
}
