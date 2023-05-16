package com.likedancesport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LearningVideoTranscodingJobCompleteHandlerApplication {
    public static void main(String[] args) {
        System.out.println("----- STARTUP");
        SpringApplication.run(LearningVideoTranscodingJobCompleteHandlerApplication.class, args);
    }
}