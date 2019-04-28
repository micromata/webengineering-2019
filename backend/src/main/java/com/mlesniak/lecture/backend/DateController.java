package com.mlesniak.lecture.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class will allow us to sent HTTP requests to /api/date and return a simple JSON object containing the
 * current timestamp.
 *
 * Question for your own research: Why can't we simply return a string?
 */
@RestController
public class DateController {
    @GetMapping("/api/date")
    public void getDate() {
        System.out.println("Date endpoint called.");
    }
}
