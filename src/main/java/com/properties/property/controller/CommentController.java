package com.properties.property.controller;

import com.properties.property.dto.CommentDTO;
import com.properties.property.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 1️⃣ Add a comment to a review
    @PostMapping("/reviews/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long id,
                                                 @RequestBody Map<String, String> body,
                                                 Principal principal) {
        String commentText = body.get("commentText");

        // Get email from logged-in user (from token)
        String userEmail = principal.getName();

        return ResponseEntity.ok(
                commentService.addCommentToReview(id, userEmail, commentText)
        );
    }

    // 2️⃣ Reply to a comment
    @PostMapping("/comments/{commentId}/replies")
    public ResponseEntity<CommentDTO> replyToComment(@PathVariable Long commentId,
                                                     @RequestBody Map<String, String> body,
                                                     Principal principal) {
        String commentText = body.get("commentText");
        String userEmail = principal.getName();

        return ResponseEntity.ok(
                commentService.replyToComment(commentId, userEmail, commentText)
        );
    }

    // 3️⃣ Update a comment
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long commentId,
                                                    @RequestBody Map<String, String> body,
                                                    Principal principal) {
        String commentText = body.get("commentText");
        String userEmail = principal.getName();

        return ResponseEntity.ok(
                commentService.updateComment(commentId, userEmail, commentText)
        );
    }

    // 4️⃣ Delete a comment
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long commentId,
                                                             Principal principal) {
        String userEmail = principal.getName();
        commentService.deleteComment(commentId, userEmail);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Comment deleted successfully",
                "data", Map.of("id", commentId)
        ));
    }
}

