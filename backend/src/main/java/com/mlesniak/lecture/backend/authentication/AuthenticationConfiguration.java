package com.mlesniak.lecture.backend.authentication;

import com.mlesniak.lecture.backend.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class AuthenticationConfiguration {
    @Bean
    @RequestScope
    public User getUser() {
        return new User();
    }
}

