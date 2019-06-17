package com.mlesniak.lecture.backend.repository;

import com.mlesniak.lecture.backend.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    @Query(value = "SELECT p FROM Post p WHERE p.title IS NOT NULL ORDER BY size(p.comments) DESC, p.createdAt DESC")
    List<Post> findPosts();
}
