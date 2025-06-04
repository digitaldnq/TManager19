package com.service;

import com.SchedulerWorker.WorkerService;
import com.orm.Categories;
import com.orm.Task;
import com.orm.TaskStatus;
import com.repository.CategoryRepository;
import com.repository.TaskRepository;
import com.script.TaskScript;
import com.retry.ExponentialStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DefaultTaskService implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private WorkerService workerService; //

    @Autowired
    private IDGenerator idGenerator;

    @Override
    public Integer addTask(TaskScript task, long scheduledTime, int maxAttempts) {
        int newId = idGenerator.getNextId();

        // создание новой задачи
        Task dbTask = new Task();
        dbTask.setId(Long.valueOf(newId));
        dbTask.setName(task.getClass().getSimpleName());
        dbTask.setDescription("Runnable task");
        dbTask.setStatus(TaskStatus.QUEUED);
        dbTask.setScheduledTime(scheduledTime);
        dbTask.setMaxRetries(maxAttempts);
        dbTask.setRetryCount(0);
        dbTask.setCreatedAt(LocalDateTime.now());

        // получаем или создаём категорию по умолчанию
        var defaultCategory = categoryRepository.findByName("DEFAULT")
                .orElseGet(() -> categoryRepository.save(new Categories("DEFAULT")));

        dbTask.setCategory(defaultCategory);

        // сейв задачи
        dbTask = taskRepository.save(dbTask);

        // планируем выполнение задачи
        workerService.executeWithRetries(task, dbTask.getId(), new ExponentialStrategy(), maxAttempts);

        return dbTask.getId() != null ? dbTask.getId().intValue() : -1;
    }

    @Override
    public boolean cancelTask(int taskId) {
        return taskRepository.findById(Long.valueOf(taskId))
                .map(task -> {
                    task.setStatus(TaskStatus.CANCELLED);
                    taskRepository.save(task);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Optional<Task> getTask(int taskId) {
        return taskRepository.findById(Long.valueOf(taskId));
    }
}