package com.mlesniak.lecture.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * This class will allow us to sent HTTP requests to /api/date and return a simple JSON object containing the
 * current timestamp.
 *
 * Question for your own research: Why can't we simply return a string?
 */
@RestController
public class DateController {
    private static final Logger LOG = LoggerFactory.getLogger(DateController.class);

    @GetMapping("/api/date")
    public Map<String, String> getDate() {
        LOG.info("Date endpoint called.");
        return Collections.singletonMap("timestamp", new Date().toString());
    }
}
