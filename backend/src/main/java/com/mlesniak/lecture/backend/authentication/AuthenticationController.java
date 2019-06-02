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
        map.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIiLCJpYXQiOjE1NTk0NDYzODksImV4cCI6MTU5MDk4MjM4OSwiYXVkIjoiIiwic3ViIjoibWxlc25pYWsiLCJuYW1lIjoiRHIuIE1pY2hhZWwgTGVzbmlhayIsImlkIjoiMSJ9.T9rexyIBnGGjqEvZtyV1E0EPbWdzgNc-X1_qHo3cCts");
        return map;
    }
}
