package com.mlesniak.lecture.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

/**
 * Controller to handle all CRUD-related post requests.
 */
@CrossOrigin
@RestController
public class PostController {
    private static final Logger LOG = LoggerFactory.getLogger(PostController.class);

    @PostMapping("/api/post")
    public Map<String, String> save(@RequestBody Post post) {
        LOG.info("Storing post: {}", post);
        // TODO ML Store value in database.
        return Collections.emptyMap();
    }

    // TODO ML Own model class

}
