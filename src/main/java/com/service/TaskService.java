package com.service;

import com.script.TaskScript;
import com.orm.Task;
import java.util.Optional;

public interface TaskService {
    Integer addTask(TaskScript task, long scheduledTime, int maxAttempts);
    boolean cancelTask(int taskId);
    Optional<Task> getTask(int taskId);
}