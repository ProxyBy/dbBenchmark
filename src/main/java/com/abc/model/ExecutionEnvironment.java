package com.abc.model;

import com.abc.dbConfig.DatabaseConfig;
import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.ExecutorService;

@Data
public class ExecutionEnvironment {
    private DatabaseConfig databaseConfig;
    private ExecutorService executorService;
    private JdbcTemplate jdbcTemplate;

    public ExecutionEnvironment(DatabaseConfig databaseConfig, ExecutorService executorService) {
        this.databaseConfig = databaseConfig;
        this.executorService = executorService;
        this.jdbcTemplate = new JdbcTemplate(databaseConfig.getDataSource());
    }
}
