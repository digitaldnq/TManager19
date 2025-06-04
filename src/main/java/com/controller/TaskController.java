package com.controller;

import com.orm.Task;
import com.script.DynamicTask;
import com.script.TaskScript;
import com.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/run")
    public Integer runTask(@RequestBody TaskDto dto) {
        TaskScript task = new DynamicTask(dto.getName(), params -> {
            if (Math.random() < 0.5) throw new RuntimeException("Ошибка выполнения");
            System.out.println("✅ Задача '" + dto.getName() + "' успешно выполнена");
        });

        return taskService.addTask(task, System.currentTimeMillis(), dto.getMaxRetries());
    }

    @PostMapping("/schedule")
    public Integer scheduleTask(@RequestBody TaskDto dto, @RequestParam long delay) {
        TaskScript task = new DynamicTask(dto.getName(), params -> {
            if (Math.random() < 0.5) throw new RuntimeException("Ошибка выполнения");
            System.out.println("✅ Задача '" + dto.getName() + "' успешно выполнена");
        });

        return taskService.addTask(task, System.currentTimeMillis() + delay, dto.getMaxRetries());
    }

    @GetMapping("/{id}")
    public Map<String, Object> getTask(@PathVariable Integer id) {
        Optional<Task> taskOptional = taskService.getTask(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            Map<String, Object> result = new HashMap<>();
            result.put("id", task.getId());
            result.put("name", task.getName());
            result.put("status", task.getStatus());
            result.put("scheduledTime", task.getScheduledTime());
            result.put("retryCount", task.getRetryCount());
            result.put("maxRetries", task.getMaxRetries());
            result.put("createdAt", task.getCreatedAt());
            return result;
        }
        return Collections.emptyMap();
    }

    @PostMapping("/{id}/cancel")
    public boolean cancelTask(@PathVariable Integer id) {
        return taskService.cancelTask(id);
    }
}