package com.likedancesport.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.Role;

@Configuration
public class IamConfig extends AbstractCdkConfig {
    @Autowired
    public IamConfig(Stack stack, StackProps stackProps) {
        super(stack, stackProps);
    }

    @Bean
    public IRole basicRole() {
        return Role.fromRoleArn(stack, "lambda-basic-role", "arn:aws:iam::066002146890:role/Rds-S3-SSM-Role");
    }
}
