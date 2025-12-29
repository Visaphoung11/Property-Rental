package com.properties.property.repository;

import com.properties.property.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserIdAndPropertyId(Long userId, Long propertyId);

    Page<Review> findByPropertyId(Long propertyId, Pageable pageable);
}
