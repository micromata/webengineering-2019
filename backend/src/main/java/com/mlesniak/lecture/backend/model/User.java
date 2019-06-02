package com.mlesniak.lecture.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "users") // Postgress does not allow the reserved name user. Thanks to docker, we saw it directly!
public class User {
    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // See JavaDoc for explanation.
    public Long id;

    String userName;
    String fullName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Create a plain user object from an arbitary one for persistence. See e.g.
     * https://stackoverflow.com/questions/26324144/spring-passing-cglib-proxy-object-to-the-repository-layer-and-hibernate
     * for other (albeit much more complicated) approaches.
     * <p>
     * The @JsonIgnore annotation is important. What happens if you omit this? Hint: it has to do with
     * Jackson's serialization approach.
     */
    @JsonIgnore
    public User getPlainUser() {
        User user = new User();
        user.id = id;
        user.fullName = fullName;
        user.userName = userName;
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
