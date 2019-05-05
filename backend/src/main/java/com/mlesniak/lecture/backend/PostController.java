package com.mlesniak.lecture.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * Controller to handle all CRUD-related post requests.
 */
@CrossOrigin
@RestController
public class PostController {
    private static final Logger LOG = LoggerFactory.getLogger(PostController.class);

    // TODO ML Constructor injection
    @Autowired
    private PostRepository postRepository;

    @PostMapping("/api/post")
    public Map<String, String> save(@RequestBody Post post) {
        // TODO ML Handle id
        LOG.info("Storing post: {}", post);
        postRepository.save(post);
        return Collections.emptyMap();
    }

    @GetMapping("/api/post")
    public Iterable<Post> save() {
        LOG.info("Retrieving all posts");
        return postRepository.findAll();
    }
}
