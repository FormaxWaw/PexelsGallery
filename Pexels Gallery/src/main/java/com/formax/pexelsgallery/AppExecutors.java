package com.formax.pexelsgallery;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {

    private static AppExecutors instance;
    private final ScheduledExecutorService networkIO = Executors.newScheduledThreadPool(3);
    private final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(3);

    public static AppExecutors get() {
        if (instance == null) {
            instance = new AppExecutors();
        }
        return instance;
    }

    public ScheduledExecutorService getNetworkIO() {
        return networkIO;
    }

    public ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }
}
