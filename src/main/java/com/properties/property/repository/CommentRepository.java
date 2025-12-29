package com.properties.property.repository;

import com.properties.property.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByReviewIdAndParentCommentIsNull(Long reviewId);
}
