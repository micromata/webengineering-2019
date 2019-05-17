package com.mlesniak.lecture.backend.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.util.Date;

/**
 * Base class with CRUD-oriented fields.
 */
@MappedSuperclass
public class RestObject {
    public Date createdAt;

    @PrePersist
    public void init() {
        if (createdAt == null) {
            createdAt = new Date();
        }
    }
}
