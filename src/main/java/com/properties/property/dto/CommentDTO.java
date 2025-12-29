package com.properties.property.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Long commentId;
    private Long parentCommentId; // null if top-level comment
    private Long reviewId;
    private String commentText;
    private UserDTO user;
    private LocalDateTime createdAt;
    private List<CommentDTO> replies = new ArrayList<>();
}