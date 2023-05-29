package com.likedancesport.temp.stacks;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.Role;
import software.constructs.Construct;

@Configuration
public class IamStack extends Stack {
    public IamStack(@Nullable RootStack scope, @Nullable String id, @Nullable StackProps props) {
        super(scope, "IamStack", props);
    }

    @Bean
    public IRole basicRole() {
        return Role.fromRoleArn(this, "lambda-basic-role", "arn:aws:iam::066002146890:role/Rds-S3-SSM-Role");
    }
}
