package com.properties.property.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ReviewResponseDTO {
    private Long reviewId;
    private int rating;
    private String initialComment;
    private LocalDateTime createdAt;
    private Long userId;
    private String fullName;
    private List<CommentDTO> comments;

}
