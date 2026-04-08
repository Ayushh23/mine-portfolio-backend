package com.shiva.portfolio.repository;

import com.shiva.portfolio.model.SiteSetting;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface SiteSettingRepository extends MongoRepository<SiteSetting, String> {
    Optional<SiteSetting> findByKey(String key);
}
