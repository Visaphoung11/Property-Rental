package com.properties.property.service;

import com.properties.property.dto.CreateReviewRequestDTO;
import com.properties.property.dto.ReviewResponseDTO;

public interface ReviewService {
    ReviewResponseDTO createReview(CreateReviewRequestDTO request, String userEmail);
}
