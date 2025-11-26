package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.demo.console.RemoteSyncTask;

/**
 * Console / administrative entry point
 * 
 * This runs only when the "console" profile is active, e.g.:
 * Use this runner to perform 12-factor style administrative tasks.
 * https://12factor.net/admin-processes
 */
@Component
@Profile("console")
public class DemoAdminRunner implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DemoAdminRunner.class);

    private final RemoteSyncTask remoteSyncTask;

    public DemoAdminRunner(RemoteSyncTask remoteSyncTask) {
        this.remoteSyncTask = remoteSyncTask;
    }

    @Override
    public void run(String... args) {
        LOG.info("Starting Administrative CommandLineRunner with {} args", args.length);

        for (int i = 0; i < args.length; ++i) {
            LOG.info("args[{}]: {}", i, args[i]);
        }

        if (args.length == 0) {
            LOG.warn("No console task specified. Expected first argument to be a task name, e.g. 'remote-sync'.");
            return;
        }

        String taskName = args[0];

        if (taskName.equals("remote-sync")) {
            LOG.info("Executing console task: remote-sync");
            remoteSyncTask.execute();
        } else {
            LOG.warn("Unknown console task: {}. Supported tasks: remote-sync", taskName);
        }

    }
}


