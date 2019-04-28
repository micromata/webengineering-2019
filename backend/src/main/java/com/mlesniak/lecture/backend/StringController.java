package com.mlesniak.lecture.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Exemplary string utility functions.
 */
@CrossOrigin
@RestController
public class StringController {
    private static final Logger LOG = LoggerFactory.getLogger(StringController.class);

    @PostMapping("/api/uppercase")
    public Map<String, String> toUppercase(@RequestBody Map<String, String> params) {
        LOG.info("Uppercase endpoint called.");
        Map<String, String> map = new HashMap<>();

        String src = params.get("string");
        map.put("string", src);

        if (src == null) {
            map.put("error", "empty string");
            return map;
        }
        map.put("result", src.toUpperCase());

        return map;
    }
}
