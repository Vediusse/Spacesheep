package io.github.zeculesu.itmo.prog5.GUI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorResource {

    // Предотвращаем создание экземпляра извне
    private ExecutorResource() {
    }

    private static class ExecutorHolder {
        private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    }

    public static ExecutorService getExecutor() {
        return ExecutorHolder.EXECUTOR;
    }

    public static void shutdownExecutor() {
        getExecutor().shutdown();
        try {
            if (!getExecutor().awaitTermination(800, TimeUnit.MILLISECONDS)) {
                getExecutor().shutdownNow();
            }
        } catch (InterruptedException e) {
            getExecutor().shutdownNow();
        }
    }
}
