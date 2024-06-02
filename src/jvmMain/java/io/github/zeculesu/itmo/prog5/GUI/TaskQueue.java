package io.github.zeculesu.itmo.prog5.GUI;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskQueue {

    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private final Thread workerThread;

    public TaskQueue() {
        workerThread = new Thread(this::processTasks);
        workerThread.setDaemon(true);
        workerThread.start();
    }

    private void processTasks() {
        try {
            while (true) {
                Runnable task = taskQueue.take();
                task.run();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void addTask(Runnable task) {
        taskQueue.add(task);
    }
}

