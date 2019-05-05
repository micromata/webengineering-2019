package com.mlesniak.lecture.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private PostRepository postRepository;

    // See e.g. https://www.vojtechruzicka.com/field-dependency-injection-considered-harmful/
    //
    // The Spring team generally advocates constructor injection as it enables one to implement application components
    // as immutable objects and to ensure that required dependencies are not null. Furthermore, constructor-injected
    // components are always returned to client (calling) code in a fully initialized state.
    @Autowired
    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @PostMapping("/api/post")
    public ResponseEntity<Map<String, String>> save(@RequestBody Post post) {
        if (post.id != null) {
            // We have explicit methods to handle single post operations, hence we prevent passing a set id value
            // using the global POST endpoint.
            //
            // Note that this is more a matter of style than a hard rule.
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        LOG.info("Storing post: {}", post);
        postRepository.save(post);
        return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyMap());
    }

    @GetMapping("/api/post")
    public Iterable<Post> save() {
        LOG.info("Retrieving all posts");
        return postRepository.findAll();
    }
}
