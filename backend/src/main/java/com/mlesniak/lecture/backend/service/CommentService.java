package com.mlesniak.lecture.backend.service;

import com.mlesniak.lecture.backend.model.Comment;
import com.mlesniak.lecture.backend.model.User;
import com.mlesniak.lecture.backend.repository.CommentRepository;
import com.mlesniak.lecture.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class CommentService {
    private static final Logger LOG = LoggerFactory.getLogger(CommentService.class);

    private CommentRepository commentRepository;
    private UserRepository userRepository;

    @Resource
    private User user;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    /**
     * Add a comment to an existing comment given its id.
     * <p>
     * Returns false if the comment could not be added. Currently, this can only happen if the parent comment
     * could not be found.
     */
    public boolean add(long id, Comment comment) {
        LOG.info("Adding comment {} to comment.id {}", comment, id);
        // Get parent comment if available.
        Optional<Comment> oParentComment = commentRepository.findById(id);
        if (!oParentComment.isPresent()) {
            return false;
        }
        Comment parentComment = oParentComment.get();

        // Add comment to parent comment.
        parentComment.getComments().add(comment);

        // Update all releated entities.
        commentRepository.save(comment);
        comment.createdBy = user.getPlainUser();
        userRepository.save(comment.createdBy);
        commentRepository.save(parentComment);

        return true;
    }
}
