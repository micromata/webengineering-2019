package com.mlesniak.lecture.backend.controller;

import com.mlesniak.lecture.backend.model.Comment;
import com.mlesniak.lecture.backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * Controller to allow posting comments under comments.
 */
@CrossOrigin
@RestController
public class CommentController {
    private CommentService commentService;
    
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/api/comment/{id}/comment")
    public ResponseEntity<Map<String, Object>> addComment(@PathVariable("id") long id, @RequestBody Comment comment) {
        boolean success = commentService.add(id, comment);
        if (!success) {
            // We only have a single error case, hence a boolean suffices.
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Parent comment not found"));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Collections.singletonMap("comment", comment));
    }
}
