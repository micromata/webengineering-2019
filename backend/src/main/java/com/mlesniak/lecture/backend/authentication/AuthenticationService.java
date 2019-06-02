package com.mlesniak.lecture.backend.authentication;

import com.github.scribejava.apis.GitHubApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.mlesniak.lecture.backend.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthenticationService {
    private final OAuth20Service service;

    @Resource
    private User user;

    public AuthenticationService() {
        // See https://github.com/settings/developers (OAuth Apps) for your personal clientid and secret.
        String clientid = System.getenv("CLIENTID");
        String secret = System.getenv("SECRET");

        this.service = new ServiceBuilder(clientid)
                .apiSecret(secret)
                .build(GitHubApi.instance());
    }

    public String getAuthenticationURL() {
        return service.getAuthorizationUrl();
    }

    public boolean isAuthenticated() {
        return user.getUserName() != null;
    }

    public User getUser() {
        return user;
    }
}
