package com.properties.property.service;

import com.properties.property.dto.CommentDTO;
import com.properties.property.dto.CreateReviewRequestDTO;
import com.properties.property.dto.PaginatedResponse;
import com.properties.property.dto.ReviewResponseDTO;
import com.properties.property.exception.ResourceNotFoundException;
import com.properties.property.mapper.CommentMapper;
import com.properties.property.model.Property;
import com.properties.property.model.Review;
import com.properties.property.model.UserModel;
import com.properties.property.repository.CommentRepository;
import com.properties.property.repository.PropertyRepository;
import com.properties.property.repository.ReviewRepository;
import com.properties.property.repository.UserReposiitory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
          // Constructor injections
    private final ReviewRepository reviewRepository;
    private final UserReposiitory userReposiitory;
    private final PropertyRepository propertyRepository;
    private final CommentRepository commentRepository;


    @Override
    public ReviewResponseDTO createReview(CreateReviewRequestDTO request, String userEmail) {
          // Find user and property models
        UserModel user = userReposiitory.findByEmail(userEmail);
        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(()-> new ResourceNotFoundException("Property not found"));

        boolean alreadyReviewed = reviewRepository.existsByUserIdAndPropertyId(user.getId(), property.getId());
        // logic if that property has already reviewed
        if(alreadyReviewed){
            throw new ResourceNotFoundException("User already review this property");

        }

        // instance to create object by using builder
        Review review = Review.builder()
                .rating(request.getRating())
                .comment(request.getInitialComment())
                .user(user)
                .property(property)
                .createdAt(LocalDateTime.now())
                .build();
        reviewRepository.save(review);

        return ReviewResponseDTO.builder()
                .reviewId(review.getId())
                .rating(review.getRating())
                .initialComment(review.getComment())
                .createdAt(review.getCreatedAt())
                .userId(user.getId())
                .fullName(user.getFullName())
                .build();

    }

    @Override
    public PaginatedResponse<ReviewResponseDTO> getReviewsByProperty(Long propertyId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Review> reviewPage = reviewRepository.findByPropertyId(propertyId, pageable);

        List<ReviewResponseDTO> reviews = reviewPage.getContent().stream().map(review -> {
            List<CommentDTO> comments = commentRepository
                    .findByReviewIdAndParentCommentIsNull(review.getId())
                    .stream()
                    .map(CommentMapper::toDTO)
                    .toList();

            return ReviewResponseDTO.builder()
                    .reviewId(review.getId())
                    .rating(review.getRating())
                    .initialComment(review.getComment())
                    .createdAt(review.getCreatedAt())
                    .userId(review.getUser().getId())
                    .fullName(review.getUser().getFullName())
                    .comments(comments)
                    .build();
        }).toList();

        return PaginatedResponse.<ReviewResponseDTO>builder()
                .content(reviews)
                .page(reviewPage.getNumber())
                .size(reviewPage.getSize())
                .totalElements(reviewPage.getTotalElements())
                .totalPages(reviewPage.getTotalPages())
                .last(reviewPage.isLast())
                .build();
    }
}



