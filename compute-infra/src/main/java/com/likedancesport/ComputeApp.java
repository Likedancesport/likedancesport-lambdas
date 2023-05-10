package com.likedancesport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

@SpringBootApplication
public class ComputeApp {

    public static void main(final String[] args) {
        App app = SpringApplication.run(ComputeApp.class, args).getBean(App.class);

        app.synth();
    }
}

