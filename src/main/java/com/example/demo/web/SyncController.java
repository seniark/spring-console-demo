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
public class SyncController {

    private static final Logger LOG = LoggerFactory.getLogger(SyncController.class);

    @GetMapping("/sync")
    public String sync() {
        LOG.info("Handling scheduled sync request");
        return "Sync Completed Successfully...";
    }
}
