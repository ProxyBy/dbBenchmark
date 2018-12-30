package com.abc.services;

import com.abc.model.BenchmarkQueryReport;
import com.abc.model.ExecutionEnvironment;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class BenchmarkRunner {
    private ExecutionEnvironment executionEnvironment;
    private String sql;

    public BenchmarkQueryReport run() {
        log.info("Benchmark for {} is running", executionEnvironment.getDatabaseConfig().getName());
        BenchmarkQueryReport benchmarkQueryReport = new BenchmarkQueryReport();
        try {
            benchmarkQueryReport.setDbName(executionEnvironment.getDatabaseConfig().getName());
            long startTime = System.currentTimeMillis();
            executionEnvironment.getJdbcTemplate().execute(sql);
            benchmarkQueryReport.setExecutionTime(System.currentTimeMillis() - startTime);
            benchmarkQueryReport.setSuccess(true);
        } catch (Exception ex) {
            log.info("Benchmark for {} has errors: {}", executionEnvironment.getDatabaseConfig().getName(), ex);
            benchmarkQueryReport.getErrors().add(ex.getMessage());
        }
        log.info("Benchmark for {} is finished", executionEnvironment.getDatabaseConfig().getName());
        return benchmarkQueryReport;
    }
}
