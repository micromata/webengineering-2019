package com.mlesniak.lecture.backend;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Comment {
    public Comment() {
        createdAt = new Date();
    }

    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // See JavaDoc for explanation.
    public Long id;

    public Date createdAt;
    public String title;
    // Currenlty we will use the content field for the URL.
    // TODO ML Rename to URL and use a dedicated class for comments.
    public String content;

    // While is is viable to solely refer to a parent post it makes collection of all (transitive) comments rather
    // cumbersome. Hence we switch the direction and a post refers to all its direct comments.
    @OneToMany
    public List<Comment> comments;
    public Integer numberOfComments;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", comments=" + comments +
                ", numberOfComments=" + numberOfComments +
                '}';
    }

    /**
     * This method is called after an object is loaded using @PostLoad.
     */
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