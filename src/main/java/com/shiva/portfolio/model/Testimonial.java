package com.shiva.portfolio.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "testimonials")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Testimonial {

    @Id
    private String id;

    private String name;
    private String role;
    private String quote;
    private String image;
    private String linkedin;
}
