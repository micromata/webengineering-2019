package com.mlesniak.lecture.backend.repository;

import com.mlesniak.lecture.backend.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    // Empty for now.
}
