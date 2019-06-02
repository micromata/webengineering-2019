package com.mlesniak.lecture.backend.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.GitHubApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.mlesniak.lecture.backend.model.User;
import com.mlesniak.lecture.backend.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class AuthenticationService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

    @Resource
    private User user;
    private UserRepository userRepository;
    private final OAuth20Service service;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        // See https://github.com/settings/developers (OAuth Apps) for your personal clientid and secret.
        String clientid = System.getenv("CLIENTID");
        String secret = System.getenv("SECRET");

        this.userRepository = userRepository;
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

    public String retrieveToken(String code) {
        OAuth2AccessToken token;
        try {
            token = service.getAccessToken(code);
        } catch (InterruptedException | ExecutionException | IOException e) {
            LOG.warn("Unable to retrieve access token for code={}", code, e);
            return null;
        }

        Optional<User> oUser = enrichUser(token);
        if (!oUser.isPresent()) {
            return null;
        }
        User user = oUser.get();

        // Store in database to have a unique id.
        Optional<User> u = userRepository.findByUserName(user.getUserName());
        if (!u.isPresent()) {
            userRepository.save(user);
        }
        user = u.get();

        // TODO ML Use environment variable for key.
        // Base64 encoded qwertyuiopasdfghjklzxcvbnm123456
        String secretKey = "cXdlcnR5dWlvcGFzZGZnaGprbHp4Y3Zibm0xMjM0NTY=";
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey.getBytes()));

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("name", user.getFullName());
        claims.put("sub", user.getUserName());
        String jwtToken = Jwts.builder()
                .addClaims(claims)
                .signWith(key)
                .compact();

        return jwtToken;
    }

    private Optional<User> enrichUser(OAuth2AccessToken token) {
        User newUser = new User();

        OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.github.com/user");
        service.signRequest(token, request);
        try {
            Response response = service.execute(request);
            String body = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = mapper.readValue(body, Map.class);
            newUser.setUserName(map.get("login"));
            newUser.setFullName(map.get("name"));
            return Optional.of(newUser);
        } catch (InterruptedException | ExecutionException | IOException e) {
            LOG.warn("Unable to retrieve user data for token={}", token.getAccessToken(), e);
            return Optional.empty();
        }
    }
}
