package com.mlesniak.lecture.backend.controller;

import com.mlesniak.lecture.backend.model.Comment;
import com.mlesniak.lecture.backend.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Controller to allow posting comments under comments.
 */
@CrossOrigin
@RestController
public class CommentController {
    private static final Logger LOG = LoggerFactory.getLogger(CommentController.class);

    // TODO ML We should use a service here.
    private CommentRepository commentRepository;

    @Autowired
    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Add a comment to an existing comment given its id.
     */
    @PostMapping("/api/comment/{id}/comment")
    public ResponseEntity<Map<String, Object>> addComment(@PathVariable("id") long id, @RequestBody Comment comment) {
        LOG.info("Adding comment {} to comment.id {}", comment, id);
        // Get parent comment if available.
        Optional<Comment> oParentComment = commentRepository.findById(id);
        if (!oParentComment.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Parent comment not found"));
        }
        Comment parentComment = oParentComment.get();

        // Add comment to parent comment.
        parentComment.getComments().add(comment);

        // Update both.
        commentRepository.save(comment);
        commentRepository.save(parentComment);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Collections.singletonMap("comment", comment));
    }
}
