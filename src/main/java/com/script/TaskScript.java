package com.script;

import java.util.Map;

public interface TaskScript {
    void execute(Map<String, Object> params) throws Exception;
}
