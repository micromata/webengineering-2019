package com.mlesniak.lecture.backend;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    @Query(value = "SELECT p FROM Post p WHERE p.title IS NOT NULL")
    List<Post> findPosts();

//    @Query(value = "SELECT p FROM Post p WHERE p.parent.id = :id")
//    List<Post> findComments(@Param("id") long id);
}
