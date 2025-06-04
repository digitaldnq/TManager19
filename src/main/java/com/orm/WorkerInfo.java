package com.orm;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "workers")
public class WorkerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "worker_name", nullable = false, unique = true)
    private String workerName;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "active_tasks", nullable = false)
    private int activeTasks;

    @Column(name = "last_heartbeat", nullable = false)
    private LocalDateTime lastHeartbeat;

    public WorkerInfo() {

    }

    public WorkerInfo(String workerName, String status, int activeTasks, LocalDateTime lastHeartbeat) {
        this.workerName = workerName;
        this.status = status;
        this.activeTasks = activeTasks;
        this.lastHeartbeat = lastHeartbeat;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getActiveTasks() {
        return activeTasks;
    }

    public void setActiveTasks(int activeTasks) {
        this.activeTasks = activeTasks;
    }

    public LocalDateTime getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(LocalDateTime lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }
}