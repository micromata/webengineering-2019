package com.mlesniak.lecture.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
public class Comment extends RestObject implements Commentable {
    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // See JavaDoc for explanation.
    public Long id;

    @Column(length = 4096)
    public String comment;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public User createdBy;

    /**
     * Note: While I favor consistency this is (or might?) be the case where making an exception from the rule is ok for
     * now, since our classes are still small. Do not hesitate to replace field access with proper getter and setter
     * when (not if!) the application grows.
     */
    @OneToMany
    @OrderBy("createdAt DESC")
    private List<Comment> comments;
    public Integer numberOfComments;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", createdBy=" + createdBy +
                ", comments=" + comments +
                ", numberOfComments=" + numberOfComments +
                ", createdAt=" + createdAt +
                '}';
    }

    @PostLoad
    public void computeStatistics() {
        numberOfComments = countComments();
    }

    @Override
    public List<Comment> getComments() {
        return comments;
    }

    @Override
    public int countComments() {
        // Number of comments in children.
        int sum = 0;
        for (Comment comment : getComments()) {
            sum += comment.countComments();
        }

        // Add own comment number.
        return sum + getComments().size();
    }
}