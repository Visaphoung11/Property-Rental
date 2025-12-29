package com.properties.property.repository;

import com.properties.property.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserIdAndPropertyId(Long userId, Long propertyId);

    List<Review> findByPropertyId(Long propertyId);
}
