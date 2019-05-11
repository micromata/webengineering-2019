package com.mlesniak.lecture.backend;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
public class Post {
    public Post() {
        createdAt = new Date();
    }

    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // See JavaDoc for explanation.
    public Long id;

    public Date createdAt;
    public String title;
    public String content;

    // While is is viable to solely refer to a parent post it makes collection of all (transitive) comments rather
    // cumbersome. Hence we switch the direction and a post refers to all its direct comments.
    @OneToMany
    public List<Post> comments;
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
}