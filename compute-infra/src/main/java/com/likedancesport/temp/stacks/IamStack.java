package com.likedancesport.temp.stacks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.NestedStack;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.Role;

@Configuration
public class IamStack extends NestedStack {
    @Autowired
    public IamStack(RootStack scope) {
        super(scope, "IamStack");
    }

    @Bean
    public IRole basicRole() {
        return Role.fromRoleArn(this, "lambda-basic-role", "arn:aws:iam::066002146890:role/Rds-S3-SSM-Role");
    }
}
