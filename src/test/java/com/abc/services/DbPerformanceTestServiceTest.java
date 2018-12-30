package com.abc.services;

import com.abc.dbConfig.DatabaseConfig;
import com.abc.dbConfig.DatabaseEnvironments;
import com.abc.model.BenchmarkQueryReport;
import com.abc.model.BenchmarkRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DbPerformanceTestServiceTest {
    @Mock
    RunnerCreator runnerCreator;

    @Mock
    BenchmarkRunner benchmarkRunner;

    @InjectMocks
    DbPerformanceTestService dbPerformanceTestService;

    @Before
    public void setUp() throws Exception {
        DatabaseConfig databaseConfig1 =
                new DatabaseConfig(
                        "testDbName1",
                        "root",
                        "user",
                        "com.mysql.cj.jdbc.Driver",
                        "jdbc:mysql://localhost:3306/testDB?insecureAuth=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false");
        DatabaseConfig databaseConfig2 =
                new DatabaseConfig(
                        "testDbName2",
                        "root",
                        "user",
                        "com.mysql.cj.jdbc.Driver",
                        "jdbc:mysql://localhost:3306/testDB?insecureAuth=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false");
        List environments = new ArrayList();
        environments.add(databaseConfig1);
        environments.add(databaseConfig2);
        DatabaseEnvironments databaseEnvironments = new DatabaseEnvironments();
        databaseEnvironments.setEnvironments(environments);
        dbPerformanceTestService = new DbPerformanceTestService(databaseEnvironments, new RunnerCreator());
        MockitoAnnotations.initMocks(this);
        when(runnerCreator.getBenchmarkRunner(any(), any())).thenReturn(benchmarkRunner);
    }

    @Test
    public void processDbWithIncorrectName() {
        List<BenchmarkQueryReport> actualBenchmarkQueryReports =
                dbPerformanceTestService.process(new BenchmarkRequest("select 1 from Customer", "incorrectDbName"));
        assertEquals(0, actualBenchmarkQueryReports.size());
    }


    @Test
    public void processAllDb() {
        BenchmarkQueryReport benchmarkQueryReport = new BenchmarkQueryReport();
        benchmarkQueryReport.setSuccess(true);
        benchmarkQueryReport.setExecutionTime(100L);
        benchmarkQueryReport.setDbName("testDbName2");

        when(benchmarkRunner.run()).thenReturn(benchmarkQueryReport);

        List<BenchmarkQueryReport> actualBenchmarkQueryReports =
                dbPerformanceTestService.process(new BenchmarkRequest("select 1 from Customer", null));
        assertEquals(2, actualBenchmarkQueryReports.size());
        assertTrue(actualBenchmarkQueryReports.get(0).isSuccess());
        assertEquals(100, actualBenchmarkQueryReports.get(0).getExecutionTime());
        assertEquals("testDbName2", actualBenchmarkQueryReports.get(0).getDbName());
        assertEquals(0, actualBenchmarkQueryReports.get(0).getErrors().size());
        assertTrue(actualBenchmarkQueryReports.get(1).isSuccess());
        assertEquals(100, actualBenchmarkQueryReports.get(1).getExecutionTime());
        assertEquals("testDbName2", actualBenchmarkQueryReports.get(1).getDbName());
        assertEquals(0, actualBenchmarkQueryReports.get(1).getErrors().size());
    }


    @Test
    public void processParticularDb() {
        BenchmarkQueryReport benchmarkQueryReport = new BenchmarkQueryReport();
        benchmarkQueryReport.setSuccess(true);
        benchmarkQueryReport.setExecutionTime(100L);
        benchmarkQueryReport.setDbName("testDbName");

        when(benchmarkRunner.run()).thenReturn(benchmarkQueryReport);

        List<BenchmarkQueryReport> actualBenchmarkQueryReports =
                dbPerformanceTestService.process(new BenchmarkRequest("select 1 from Customer", null));
        assertEquals(2, actualBenchmarkQueryReports.size());
        assertTrue(actualBenchmarkQueryReports.get(0).isSuccess());
        assertEquals(100, actualBenchmarkQueryReports.get(0).getExecutionTime());
        assertEquals("testDbName", actualBenchmarkQueryReports.get(0).getDbName());
        assertEquals(0, actualBenchmarkQueryReports.get(0).getErrors().size());
    }
}