package com.example.demo.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Console task that performs a remote sync by calling a remote /api/sync endpoint.
 */
@Component
@Profile("console")
public class RemoteSyncTask {

    private static final Logger LOG = LoggerFactory.getLogger(RemoteSyncTask.class);

    private final RestClient restClient;
    private final String syncUrl;

    public RemoteSyncTask(@Value("${console.remote-sync.url}") String syncUrl) {
        this.restClient = RestClient.create();
        this.syncUrl = syncUrl;
    }

    /**
     * Execute the "remote-sync" task: perform a GET to the configured API endpoint.
     */
    public void execute() {
        LOG.info("Starting remote-sync task. Calling {}", syncUrl);

        String responseBody = restClient.get()
                .uri(syncUrl)
                .retrieve()
                .body(String.class);

        LOG.info("remote-sync completed. Response: {}", responseBody);
    }
}


