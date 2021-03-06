package com.mlesniak.lecture.backend.controller;

import com.mlesniak.lecture.backend.model.Comment;
import com.mlesniak.lecture.backend.model.Post;
import com.mlesniak.lecture.backend.model.User;
import com.mlesniak.lecture.backend.repository.CommentRepository;
import com.mlesniak.lecture.backend.repository.PostRepository;
import com.mlesniak.lecture.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller to handle all CRUD-related post requests.
 */
@CrossOrigin
@RestController
public class PostController {
    private static final Logger LOG = LoggerFactory.getLogger(PostController.class);

    @Resource
    private User user;

    // TODO ML Use the same service pattern here as in CommentController.
    private PostRepository postRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;

    @Value("${debug.authentication:false}")
    private boolean debugAuthentication;

    // See e.g. https://www.vojtechruzicka.com/field-dependency-injection-considered-harmful/
    //
    // The Spring team generally advocates constructor injection as it enables one to implement application components
    // as immutable objects and to ensure that required dependencies are not null. Furthermore, constructor-injected
    // components are always returned to client (calling) code in a fully initialized state.
    @Autowired
    public PostController(PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/api/post")
    public ResponseEntity<Map<String, String>> addPost(@RequestBody Post post) {
        LOG.info("Storing post: {}", post);
        post.createdBy = user.getPlainUser();
        userRepository.save(post.createdBy);
        postRepository.save(post);
        return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyMap());
    }

    /**
     * Return all posts without their transitive comments.
     */
    @GetMapping("/api/post")
    public List<Post> listPosts() {
        LOG.info("Retrieving all posts");
        // We can later use our findPosts method to simply return the basic information instead of gathering
        // everything and filter already pre-filled objects.
        List<Post> postsWithoutComments = postRepository.findPosts().stream().map(post -> {
            post.getComments().clear();
            return post;
        }).collect(Collectors.toList());

        return postsWithoutComments;
    }

    /**
     * Retrieve details to a single post.
     */
    @GetMapping("/api/post/{id}")
    public ResponseEntity<Post> getPostDetails(@PathVariable("id") long id) {
        HashMap<String, Object> map = new HashMap<>();
        LOG.info("Retrieving details for post {}", id);

        // Get main post (which will include all comments).
        Optional<Post> oPost = postRepository.findById(id);
        if (!oPost.isPresent()) {
            LOG.warn("Post with id {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Post post = oPost.get();

        return ResponseEntity.ok(post);
    }

    /**
     * Add method to delete everything. Only used for testing and will later be removed.
     */
    @DeleteMapping("/api/post")
    public ResponseEntity<Void> deleteAllPosts() {
        LOG.warn("Deleting ALL posts and comments");
        postRepository.deleteAll();
        commentRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

    /**
     * Add a comment to an existing post given its id.
     */
    @PostMapping("/api/post/{id}/comment")
    public ResponseEntity<Map<String, Object>> addPostComment(@PathVariable("id") long id, @RequestBody Comment comment) {
        LOG.info("Adding comment {} to post.id {}", comment, id);
        // Get parent post if available.
        Optional<Post> oParentPost = postRepository.findById(id);
        if (!oParentPost.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Parent post not found"));
        }
        Post parentPost = oParentPost.get();

        // Add comment to parent post.
        parentPost.getComments().add(comment);

        // Update all releated entities.
        commentRepository.save(comment);
        comment.createdBy = user.getPlainUser();
        userRepository.save(comment.createdBy);
        postRepository.save(parentPost);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Collections.singletonMap("post", comment));
    }
}
