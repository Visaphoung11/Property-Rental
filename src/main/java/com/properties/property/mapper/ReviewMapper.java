package com.properties.property.mapper;

import com.properties.property.dto.ReviewResponseDTO;
import com.properties.property.model.Review;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ReviewMapper {

    public static ReviewResponseDTO toDTO(Review review) {

        return ReviewResponseDTO.builder()
                .reviewId(review.getId())
                .rating(review.getRating())
                .initialComment(review.getComment())
                .createdAt(review.getCreatedAt())
                .userId(review.getUser().getId())
                .fullName(review.getUser().getFullName())
                .comments(
                        review.getComments() == null
                                ? new ArrayList<>()
                                : review.getComments().stream()
                                .filter(c -> c.getParentComment() == null)
                                .map(CommentMapper::toDTO)
                                .collect(Collectors.toList())
                )
                .build();
    }
}