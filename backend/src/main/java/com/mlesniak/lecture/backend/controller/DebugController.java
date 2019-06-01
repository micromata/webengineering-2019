package com.mlesniak.lecture.backend.controller;

import com.mlesniak.lecture.backend.repository.CommentRepository;
import com.mlesniak.lecture.backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;

@CrossOrigin
@RestController
public class DebugController {
    private PostRepository postRepository;
    private CommentRepository commentRepository;

    @Autowired
    public DebugController(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    /**
     * Debug endpoint for scripts. Will later be removed.
     */
    @GetMapping("/api/debug/all")
    public Iterable<Object> getIdRanges() {
        LinkedList<Object> objects = new LinkedList<>();
        postRepository.findAll().forEach(c -> objects.add(c));
        commentRepository.findAll().forEach(c -> objects.add(c));
        return objects;
    }
}
