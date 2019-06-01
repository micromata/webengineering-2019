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
        map.put("token", "TOKEN-" + code);
        return map;
    }
}
