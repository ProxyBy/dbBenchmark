package com.abc.services;

import com.abc.model.BenchmarkQueryReport;
import com.abc.model.BenchmarkRequest;
import com.abc.model.ExecutionEnvironment;
import com.abc.dbConfig.DatabaseEnvironments;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Component
@Slf4j
public class DbPerformanceTestService {
    private Map<String, ExecutionEnvironment> environments;
    private RunnerCreator runnerCreator;

    public DbPerformanceTestService(DatabaseEnvironments databaseEnvironments, RunnerCreator runnerCreator) {
        this.runnerCreator = runnerCreator;
        environments = new HashMap<>();
        databaseEnvironments.getEnvironments()
                .forEach(dbConfig -> environments.put(dbConfig.getName(), new ExecutionEnvironment(dbConfig, Executors.newSingleThreadExecutor())));
        environments.values().forEach(value -> value.getExecutorService().submit(() -> runnerCreator.getConnectionInitRunner(value).run()));
    }

    private List<CompletableFuture<BenchmarkQueryReport>> dbBenchmark(BenchmarkRequest benchmarkRequest) {
        List<CompletableFuture<BenchmarkQueryReport>> futures = environments.entrySet().stream()
                .filter(env -> benchmarkRequest.getDbName() == null || env.getKey().equals(benchmarkRequest.getDbName()))
                .map(env -> CompletableFuture.supplyAsync(() -> runnerCreator.getBenchmarkRunner(env.getValue(), benchmarkRequest.getQuery())
                        .run(), env.getValue().getExecutorService())).collect(Collectors.toList());
        return futures;
    }

    private List<BenchmarkQueryReport> getResponseReports(List<CompletableFuture<BenchmarkQueryReport>> completableFutures) { // TODO get and return
        return completableFutures.stream().map(env -> {
            try {
                return env.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                //return new BenchmarkQueryReport().getErrors().add(e.getMessage());
            }
            return null;
        }).collect(Collectors.toList());
    }

    public List<BenchmarkQueryReport> process(BenchmarkRequest benchmarkRequest) {
        return getResponseReports(dbBenchmark(benchmarkRequest));
    }
}
