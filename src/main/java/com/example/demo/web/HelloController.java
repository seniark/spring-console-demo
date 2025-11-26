package com.example.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple REST controller exposed by the standard Spring Boot application.
 */
@RestController
@RequestMapping("/api")
public class HelloController {

    private static final Logger LOG = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public String hello() {
        LOG.info("Handling /api/hello request");
        return "Hello from the standard Spring Boot REST application!";
    }
}
