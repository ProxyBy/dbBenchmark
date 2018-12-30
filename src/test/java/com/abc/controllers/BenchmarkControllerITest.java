package com.abc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BenchmarkControllerITest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void particularDbBenchmarkCalculation() {
        try {
            this.mockMvc.perform(post("/benchmark")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"query\":\"select * from Customer\",\"dbName\":\"mysql1Test\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].dbName", is("mysql1Test")))
                    .andExpect(jsonPath("$[0].success", is(true)));
        } catch (Exception e) {
            log.error("Test {} exception: {}", "particularDbBenchmarkCalculation", e);
        }
    }

    @Test
    public void allDbBenchmarkCalculation() {
        try {
            this.mockMvc.perform(post("/benchmark")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"query\":\"select * from Customer\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[1].dbName", is("mysql1Test")))
                    .andExpect(jsonPath("$[1].success", is(true)))
                    .andExpect(jsonPath("$[0].dbName", is("mysql2Test")))
                    .andExpect(jsonPath("$[0].success", is(true)));
        } catch (Exception e) {
            log.error("Test {} exception: {}", "particularDbBenchmarkCalculation", e);
        }
    }
}