package com.mlesniak.lecture.backend;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Post {
    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // See JavaDoc for explanation.
    public Long id;

    public Date createdAt;
    public String title;
    public String url;

    @OneToMany
    public List<Comment> comments;
    public Integer numberOfComments;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", comments=" + comments +
                ", numberOfComments=" + numberOfComments +
                '}';
    }

    @PostLoad
    public void computeStatistics() {
        numberOfComments = countComments();
        createdAt = new Date();
    }

    /**
     * Count number of all comments and sub-comments.
     */
    private int countComments() {
        // Number of comments in children.
        int sum = 0;
        for (Comment comment : comments) {
            // TODO ML Add interface for counts
            sum += comment.countComments();
        }

        // Add own comment number.
        return sum + comments.size();
    }
}