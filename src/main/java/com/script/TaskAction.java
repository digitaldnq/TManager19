package com.script;

import java.util.Map;

@FunctionalInterface
public interface TaskAction {
    void run(Map<String, Object> params) throws Exception;
}