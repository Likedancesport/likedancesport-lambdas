package com.likedancesport.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.s3.Bucket;

@Configuration
public class AppConfig {
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
}
