package com.properties.property.mapper;

import com.properties.property.dto.CommentDTO;
import com.properties.property.dto.UserDTO;
import com.properties.property.model.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    public static CommentDTO toDTO(Comment comment) {
        return CommentDTO.builder()
                .commentId(comment.getId())
                .reviewId(comment.getReview().getId())
                .parentCommentId(comment.getParentComment() != null
                        ? comment.getParentComment().getId()
                        : null)
                .commentText(comment.getCommentText())
                .createdAt(comment.getCreatedAt())
                .user(new UserDTO(comment.getUser().getId(), comment.getUser().getFullName()))
                .replies(
                        comment.getReplies() == null
                                ? List.of()
                                : comment.getReplies().stream()
                                .map(CommentMapper::toDTO)
                                .toList()
                )

                .build();
    }



}
