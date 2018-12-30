package com.abc.services;

import com.abc.model.ExecutionEnvironment;
import org.springframework.stereotype.Component;

@Component
public class RunnerCreator {
    public BenchmarkRunner getBenchmarkRunner(ExecutionEnvironment executionEnvironment, String query) {
        return new BenchmarkRunner(executionEnvironment, query);
    }

    public ConnectionInitRunner getConnectionInitRunner(ExecutionEnvironment executionEnvironment) {
        return new ConnectionInitRunner(executionEnvironment);
    }
}
