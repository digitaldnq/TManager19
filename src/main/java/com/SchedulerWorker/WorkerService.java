package com.SchedulerWorker;

import com.script.TaskScript;
import com.retry.RetryStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkerService {

    private final List<Worker> workers = new ArrayList<>();

    public WorkerService() {
        workers.add(new Worker());
    }



    public void executeWithRetries(TaskScript task, Long taskId, RetryStrategy strategy, int maxAttempts) {
        if (workers.isEmpty()) {
            throw new IllegalStateException("Нет доступных воркеров");
        }

        Worker worker = workers.get(0);
        worker.executeWithRetries(task, taskId, strategy, maxAttempts);
    }
}