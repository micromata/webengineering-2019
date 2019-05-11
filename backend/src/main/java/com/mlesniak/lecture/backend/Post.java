package com.mlesniak.lecture.backend;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
public class Post {
    public Post() {
        createdAt = new Date();
    }

    @Id
    @GeneratedValue
    public Long id;

    public Date createdAt;
    public String title;
    public String content;

    @OneToOne
    public Post parent;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", parent=" + parent +
                '}';
    }
}