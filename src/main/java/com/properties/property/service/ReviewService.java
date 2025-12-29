package com.properties.property.service;

import com.properties.property.dto.CreateReviewRequestDTO;
import com.properties.property.dto.ReviewResponseDTO;

import java.util.List;

public interface ReviewService {
    ReviewResponseDTO createReview(CreateReviewRequestDTO request, String userEmail);
    List<ReviewResponseDTO> getReviewsByProperty(Long propertyId);
}
