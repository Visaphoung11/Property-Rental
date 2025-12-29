package com.properties.property.controller;

import com.properties.property.dto.CreateReviewRequestDTO;
import com.properties.property.dto.ReviewResponseDTO;
import com.properties.property.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")

    public ResponseEntity<ReviewResponseDTO> createReview (  @RequestBody CreateReviewRequestDTO request,
                                                             Authentication authentication){
      String email = authentication.getName();

        return ResponseEntity.ok(reviewService.createReview(request, email));
    }

}
