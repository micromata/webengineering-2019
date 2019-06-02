package com.mlesniak.lecture.backend.authentication;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
public class AuthenticationController {
    // TODO ML Use the code to retrieve actual user information
    @GetMapping("/api/authentication/callback")
    public Map<String, String> getUserInfor(@RequestParam("code") String code) {
        Map<String, String> map = new HashMap<>();
        // Create an actual JWT token, e.g. see http://jwtbuilder.jamiekurtz.com/ for a generator online.
        map.put("token", "yJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        return map;
    }
}
