package com.shiva.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "site_settings")
public class SiteSetting {
    @Id
    private String id;
    private String key;
    private String value;

    public SiteSetting(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
