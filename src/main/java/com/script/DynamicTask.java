package com.script;

import java.util.Map;

public class DynamicTask implements TaskScript {

    private final String name;
    private final TaskAction action;

    public DynamicTask(String name, TaskAction action) {
        this.name = name;
        this.action = action;
    }

    @Override
    public void execute(Map<String, Object> params) throws Exception {
        action.run(params);
    }

    public String getName() {
        return name;
    }
}
