package com.abc.services;

import com.abc.dbConfig.DatabaseConfig;
import com.abc.model.BenchmarkQueryReport;
import com.abc.model.ExecutionEnvironment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Executors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class BenchmarkRunnerTest {
    BenchmarkRunner benchmarkRunner;

    @Test
    public void runSuccess() {
        DatabaseConfig databaseConfig =
                new DatabaseConfig("testDbName", "root", "user", "com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/testDB?insecureAuth=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false");
        ExecutionEnvironment executionEnvironment =
                new ExecutionEnvironment(databaseConfig, Executors.newSingleThreadExecutor());
        benchmarkRunner = new BenchmarkRunner(executionEnvironment, "select * from Customer");
        BenchmarkQueryReport actualBenchmarkQueryReport = benchmarkRunner.run();
        assertEquals("testDbName", actualBenchmarkQueryReport.getDbName());
        assertTrue(actualBenchmarkQueryReport.isSuccess());
        assertEquals(0, actualBenchmarkQueryReport.getErrors().size());
        assertTrue(actualBenchmarkQueryReport.getExecutionTime() > 0L);
    }

    @Test
    public void runWithError() {
        DatabaseConfig databaseConfig =
                new DatabaseConfig("testDbName", "root", "user", "incorrectDriver", "jdbc:mysql://localhost:3306/testDB?insecureAuth=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false");
        ExecutionEnvironment executionEnvironment =
                new ExecutionEnvironment(databaseConfig, Executors.newSingleThreadExecutor());
        benchmarkRunner = new BenchmarkRunner(executionEnvironment, "select * from Customer");
        BenchmarkQueryReport actualBenchmarkQueryReport = benchmarkRunner.run();
        assertEquals("testDbName", actualBenchmarkQueryReport.getDbName());
        assertFalse(actualBenchmarkQueryReport.isSuccess());
        assertTrue(actualBenchmarkQueryReport.getErrors().size() == 1);
        assertTrue(actualBenchmarkQueryReport.getExecutionTime() == 0L);
    }
}