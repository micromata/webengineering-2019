package com.mlesniak.lecture.backend;

import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    // Empty for now.
}
