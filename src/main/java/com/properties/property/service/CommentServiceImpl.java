package com.properties.property.service;

import com.properties.property.dto.CommentDTO;
import com.properties.property.exception.ForbiddenOperationException;
import com.properties.property.exception.ResourceNotFoundException;
import com.properties.property.mapper.CommentMapper;
import com.properties.property.model.Comment;
import com.properties.property.model.Review;
import com.properties.property.model.UserModel;
import com.properties.property.repository.CommentRepository;
import com.properties.property.repository.ReviewRepository;
import com.properties.property.repository.UserReposiitory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserReposiitory userRepository;


    @Override
    public CommentDTO addCommentToReview(Long reviewId, String userEmail, String text) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        UserModel user = userRepository.findByEmail(userEmail);
        if (user == null)
            throw new ResourceNotFoundException("User not found with email: " + userEmail);

        Comment comment = Comment.builder()
                .review(review)
                .user(user)
                .commentText(text)
                .build();

        commentRepository.save(comment);

        return CommentMapper.toDTO(comment);
    }

    @Override
    public CommentDTO replyToComment(Long commentId, String userEmail, String text) {

        Comment parent = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found with id: " + commentId));

        UserModel user = userRepository.findByEmail(userEmail);
        if (user == null)
            throw new ResourceNotFoundException("User not found with email: " + userEmail);

        Comment reply = Comment.builder()
                .review(parent.getReview())
                .parentComment(parent)
                .user(user)
                .commentText(text)
                .build();

        commentRepository.save(reply);
        return CommentMapper.toDTO(reply);
    }

    @Override
    public void deleteComment(Long commentId, String userEmail) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (!comment.getUser().getEmail().equals(userEmail)) {
            throw new ForbiddenOperationException("You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);
    }

    @Override
    public CommentDTO updateComment(Long commentId, String userEmail, String text) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));


        if (!comment.getUser().getEmail().equals(userEmail)) {
            throw new ForbiddenOperationException("You are not allowed to update this comment");
        }

        comment.setCommentText(text);
        commentRepository.save(comment);

        return CommentMapper.toDTO(comment);
    }
}

