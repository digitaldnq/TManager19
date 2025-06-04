package com.service;

import org.springframework.stereotype.Component;

@Component
public class IDGenerator {

    private int currentId = 1;

    public synchronized int getNextId() {
        return currentId++;
    }
}