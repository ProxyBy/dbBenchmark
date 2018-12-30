package com.abc.controllers;

import com.abc.model.BenchmarkQueryReport;
import com.abc.model.BenchmarkRequest;
import com.abc.services.DbPerformanceTestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BenchmarkController {
    private DbPerformanceTestService dbPerformanceTestService;

    @ResponseBody
    @PostMapping("/benchmark")
    public ResponseEntity<List<BenchmarkQueryReport>> benchmarkCalculation(@RequestBody BenchmarkRequest benchmarkRequest) {
        return ResponseEntity.ok(dbPerformanceTestService.process(benchmarkRequest));
    }

}