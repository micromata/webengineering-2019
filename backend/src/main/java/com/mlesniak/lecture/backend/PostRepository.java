package com.mlesniak.lecture.backend;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    // For NOW, we use a simple native query.
    @Query(value = "SELECT * FROM Post p ORDER BY p.created_at DESC LIMIT 5", nativeQuery = true)
    List<Post> findAll();
}
