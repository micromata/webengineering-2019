package com.mlesniak.lecture.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller to handle all CRUD-related post requests.
 */
@CrossOrigin
@RestController
public class PostController {
    private static final Logger LOG = LoggerFactory.getLogger(PostController.class);

    // TODO ML We should use a service here.
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
    public ResponseEntity<Map<String, String>> getPosts(@RequestBody Post post) {
        LOG.info("Storing post: {}", post);
        postRepository.save(post);
        return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyMap());
    }

    /**
     * Return all posts without their transitive comments.
     */
    @GetMapping("/api/post")
    public List<Post> getPosts() {
        LOG.info("Retrieving all posts");
        // We can later use our findPosts method to simply return the basic information instead of gathering
        // everything and filter already pre-filled objects.
        List<Post> postsWithoutComments = postRepository.findPosts().stream().map(post -> {
            post.comments = null;
            return post;
        }).collect(Collectors.toList());

        return postsWithoutComments;
    }

//    @GetMapping("/api/post/{id}")
//    public Map<String, Object> postDetails(@PathVariable("id") long id) {
//        HashMap<String, Object> map = new HashMap<>();
//        LOG.info("Retrieving details for post {}", id);
//
//        // Get main post.
//        Optional<Post> oPost = postRepository.findById(id);
//        if (!oPost.isPresent()) {
//            // TODO ML Correct return type
//            return null;
//        }
//        map.put("post", oPost.get());
//
//        // Get all comments for top post.
//        List<Post> comments = postRepository.findComments(id);
//        map.put("comments", comments);
//
//        return map;
//    }

    /**
     * Add method to delete everything. Only used for testing and will later be removed.
     */
    @DeleteMapping("/api/post")
    public ResponseEntity<Void> deleteAll() {
        LOG.warn("Deleting ALL posts");
        postRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

    /**
     * Add a comment to an existing post given its id.
     */
    @PostMapping("/api/post/{id}/comment")
    public ResponseEntity<Map<String, Object>> addComment(@PathVariable("id") long id, @RequestBody Post post) {
        LOG.info("Adding comment {} to post.id {}", post, id);
        // Get parent post if available.
        Optional<Post> oParentPost = postRepository.findById(id);
        if (!oParentPost.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Parent post not found"));
        }
        Post parentPost = oParentPost.get();

        // Add comment to parent post.
        parentPost.comments.add(post);

        // Update both.
        postRepository.save(post);
        postRepository.save(parentPost);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Collections.singletonMap("post", post));
    }
}
