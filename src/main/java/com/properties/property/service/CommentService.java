package com.properties.property.service;

import com.properties.property.dto.CommentDTO;
public interface CommentService {
    
    
    // Use userEmail (String) instead of userId (Long)
    CommentDTO addCommentToReview(Long reviewId, String userEmail, String text);

    CommentDTO replyToComment(Long commentId, String userEmail, String text);

    void deleteComment(Long commentId, String userEmail);

    CommentDTO updateComment(Long commentId, String userEmail, String text);
}

