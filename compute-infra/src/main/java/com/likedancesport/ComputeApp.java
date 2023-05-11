package com.likedancesport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

@SpringBootApplication
public class ComputeApp {
    public static void main(final String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ComputeApp.class, args);
        App app = applicationContext.getBean(App.class);
        app.synth();
    }

    @Bean
    public App app() {
        return new App();
    }

    @Bean
    public StackProps stackProps() {
        return StackProps.builder()
                .env(Environment.builder()
                        .account("066002146890")
                        .region("eu-central-1")
                        .build())
                .build();
    }

    @Bean
    public LikedancesportStack stack() {
        return new LikedancesportStack(app(), "dev", stackProps());
    }
}

