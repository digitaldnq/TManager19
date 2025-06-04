package com.SchedulerWorker;

import com.retry.ExponentialStrategy;
import com.script.TaskScript;
import com.orm.TaskStatus;
import com.repository.TaskRepository;
import com.orm.Task;
import com.retry.RetryStrategy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Worker {

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final RetryStrategy defaultStrategy;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    public Worker(RetryStrategy defaultStrategy) {
        this.defaultStrategy = defaultStrategy;
    }

    public Worker() {
        this(new ExponentialStrategy());
    }

    public void executeWithRetries(TaskScript task, RetryStrategy strategy, int maxAttempts) {
        AtomicInteger attempt = new AtomicInteger(0);
        boolean[] success = {false};

        executor.submit(() -> {
            while (attempt.get() < maxAttempts && !success[0]) {
                try {
                    task.execute(null); // без параметров
                    success[0] = true;
                } catch (Exception e) {
                    attempt.incrementAndGet();
                    if (attempt.get() < maxAttempts) {
                        long delay = 0;
                        try {
                            delay = strategy.nextDelay(attempt.get());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }

            if (!success[0]) {
                System.err.println("Задача провалилась после " + maxAttempts + " попыток");
            }
        });
    }

    public void executeWithRetries(TaskScript task, Long taskId, RetryStrategy strategy, int maxAttempts) {
        AtomicInteger attempt = new AtomicInteger(0);
        boolean[] success = {false};

        executor.submit(() -> {
            while (attempt.get() < maxAttempts && !success[0]) {
                try {
                    task.execute(null);
                    success[0] = true;
                } catch (Exception e) {
                    attempt.incrementAndGet();
                    if (attempt.get() < maxAttempts) {
                        long delay;
                        try {
                            delay = strategy.nextDelay(attempt.get());
                        } catch (Exception e1) {
                            System.err.println("ошибка расчёта задержки " + e1.getMessage());
                            delay = 1000;
                        }
                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    updateTaskStatus(taskId, TaskStatus.FAILED, e.getMessage());
                }
            }

            if (success[0]) {
                updateTaskStatus(taskId, TaskStatus.COMPLETED, null);
            }
        });
    }

    private void updateTaskStatus(Long taskId, TaskStatus status, String errorMessage) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task != null) {
            task.setStatus(status);
            task.setErrorMessage(errorMessage);
            taskRepository.save(task);
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}