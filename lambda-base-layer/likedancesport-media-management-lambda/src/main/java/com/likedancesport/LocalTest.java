package com.likedancesport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("local")
public class LocalTest {
    public static void main(String[] args) {
        SpringApplication.run(LocalTest.class, args);
    }
}
