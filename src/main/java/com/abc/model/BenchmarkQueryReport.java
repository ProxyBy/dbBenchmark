package com.abc.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BenchmarkQueryReport {
    private long executionTime;
    private String dbName;
    private boolean success = false;
    private List<String> errors = new ArrayList<>();
}
