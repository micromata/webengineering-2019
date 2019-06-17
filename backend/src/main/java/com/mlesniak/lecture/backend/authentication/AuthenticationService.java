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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Retrieve data from GitHub and generate signed JWT tokens.
 */
@Service
public class AuthenticationService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

    @Resource
    private User user;
    private UserRepository userRepository;
    private OAuth20Service service;

    // See https://github.com/settings/developers (OAuth Apps) for your personal clientid and secret.
    @Value("${CLIENTID}")
    private String clientid;
    @Value("${SECRET}")
    private String secret;
    // An arbitrary, base64 encoded key with a (byte) length of 32 characters. Under unix, you can execute
    // echo -n "<your key>"|base64 for cleartext keys.
    @Value("${SECRET_KEY}")
    private String secretKey;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void init() {
        service = new ServiceBuilder(clientid)
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

    /**
     * Compute the JWT token given the provided OAth code by retrieving an access token, querying GitHub's user
     * API and creating a corresponding JWT token for internal use.
     */
    public String retrieveJWTToken(String code) {
        OAuth2AccessToken token = getAccessToken(code);
        if (token == null) {
            return null;
        }

        User user = retrieveUserInformation(token);
        if (user == null) {
            return null;
        }

        return createJWT(user);
    }

    /**
     * Retrieve access token from callback code for further API requests.
     */
    private OAuth2AccessToken getAccessToken(String code) {
        try {
            return service.getAccessToken(code);
        } catch (InterruptedException | ExecutionException | IOException e) {
            LOG.warn("Unable to retrieve access token for code={}", code, e);
            return null;
        }
    }

    /**
     * Get a database'd user with full user information using the provided access token.
     */
    private User retrieveUserInformation(OAuth2AccessToken token) {
        Optional<User> oUser = getUserData(token);
        if (!oUser.isPresent()) {
            return null;
        }
        User user = oUser.get();

        // Store user in database (if not already done) to have a full user object including id.
        Optional<User> u = userRepository.findByUserName(user.getUserName());
        if (!u.isPresent()) {
            userRepository.save(user);
        }
        user = u.get();
        return user;
    }

    /**
     * Create a token for the given user.
     */
    private String createJWT(User user) {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey.getBytes()));
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("name", user.getFullName());
        claims.put("sub", user.getUserName());
        return Jwts.builder()
                .addClaims(claims)
                .signWith(key)
                .compact();
    }

    /**
     * Use the access token to query GitHub's user API for username and full name.
     */
    private Optional<User> getUserData(OAuth2AccessToken token) {
        User newUser = new User();

        OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.github.com/user");
        service.signRequest(token, request);
        try {
            Response response = service.execute(request);
            String body = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(body, Map.class);
            newUser.setUserName((String) map.get("login"));
            newUser.setFullName((String) map.get("name"));
            return Optional.of(newUser);
        } catch (InterruptedException | ExecutionException | IOException e) {
            LOG.warn("Unable to retrieve user data for token={}", token.getAccessToken(), e);
            return Optional.empty();
        }
    }
}
