package com.shiva.portfolio.repository;

import com.shiva.portfolio.model.Testimonial;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonialRepository extends MongoRepository<Testimonial, String> {
}
