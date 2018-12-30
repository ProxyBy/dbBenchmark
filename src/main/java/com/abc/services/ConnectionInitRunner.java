package com.abc.services;

import com.abc.model.ExecutionEnvironment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectionInitRunner {
    private ExecutionEnvironment executionEnvironment;
    private final String TEST_SQL = "select 1 from Customer"; //TODO change query

    public ConnectionInitRunner(ExecutionEnvironment executionEnvironment) {
        this.executionEnvironment = executionEnvironment;
    }

    public void run() {
        log.info("Execution for preparing connection: {}", executionEnvironment.getDatabaseConfig().getName());
        executionEnvironment.getJdbcTemplate().execute(TEST_SQL);
    }
}
