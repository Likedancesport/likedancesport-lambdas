package com.likedancesport.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.IBucket;

@Configuration
public class RolesAndBuckets extends AbstractCdkConfig{

    @Autowired
    public RolesAndBuckets(Stack stack, StackProps stackProps) {
        super(stack, stackProps);
    }

    @Bean
    public IBucket codebaseBucket() {
        return Bucket.fromBucketArn(stack, "likedancesport-codebase", "arn:aws:s3:::likedancesport-codebase");
    }

    @Bean
    public IRole role(){
        return Role.fromRoleArn(stack, "lambda-basic-role", "arn:aws:iam::066002146890:role/Rds-S3-SSM-Role");
    }
}
