package com.mlesniak.lecture.backend.repository;

import com.mlesniak.lecture.backend.model.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    // Empty for now.
}
