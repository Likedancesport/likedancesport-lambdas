package com.likedancesport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class LearningVideoTranscodingJobCompleteHandlerApplication {
    public static void main(String[] args) {
        log.info("----- STARTUP -----");
        SpringApplication.run(LearningVideoTranscodingJobCompleteHandlerApplication.class, args);
    }
}