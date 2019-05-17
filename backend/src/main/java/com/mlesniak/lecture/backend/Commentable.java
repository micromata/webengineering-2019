package com.mlesniak.lecture.backend;

import com.mlesniak.lecture.backend.comment.Comment;

import java.util.List;

/**
 * Marks a POJO as being able to handle comments.
 */
public interface Commentable {
    /**
     * Return the number of comments.
     */
    int countComments();

    /**
     * Return a (mutable) list of comments.
     */
    List<Comment> getComments();
}
