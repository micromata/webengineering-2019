package com.mlesniak.lecture.backend.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
public class AuthenticationController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/api/authentication/callback")
    public Map<String, String> getUserInfor(@RequestParam("code") String code) {
        Map<String, String> map = new HashMap<>();

        String jwtToken = authenticationService.retrieveJWTToken(code);
        if (jwtToken == null) {
            // TODO ML Return 500.
            LOG.warn("jwtToken is null. Fix this");
        }
        map.put("token", jwtToken);
        return map;
    }

    /**
     * Return the redirect URL for OAuth.
     * <p>
     * Note that this is the place where we can add multiple authentication provider urls, e.g. using an additional
     * request parameter.
     */
    @GetMapping("/api/authentication/url")
    public Map<String, String> getAuthenticationURL() {
        Map<String, String> map = new HashMap<>();
        map.put("url", authenticationService.getAuthenticationURL());
        return map;
    }
}
