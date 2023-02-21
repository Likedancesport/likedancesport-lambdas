package com.likedancesport.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.likedancesport.service.storage.S3StorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.likedancesport")
public class S3Config {
    @Bean
    public S3StorageService s3StorageService(AmazonS3 amazonS3) {
        return new S3StorageService(amazonS3);
    }

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.defaultClient();
    }
}
