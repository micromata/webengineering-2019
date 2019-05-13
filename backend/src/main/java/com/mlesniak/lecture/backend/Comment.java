package com.mlesniak.lecture.backend;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
public class Comment extends RestObject {
    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // See JavaDoc for explanation.
    public Long id;

    public String comment;

    @OneToMany
    public List<Comment> comments;
    public Integer numberOfComments;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", comment='" + comment + '\'' +
                ", comments=" + comments +
                ", numberOfComments=" + numberOfComments +
                '}';
    }

    @PostLoad
    public void computeStatistics() {
        numberOfComments = countComments();
    }

    /**
     * Count number of all comments and sub-comments.
     */
    public int countComments() {
        // Number of comments in children.
        int sum = 0;
        for (Comment comment : comments) {
            sum += comment.countComments();
        }

        // Add own comment number.
        return sum + comments.size();
    }
}