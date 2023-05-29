package com.likedancesport.temp.stacks;

import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.TableClass;
import software.constructs.Construct;

@Configuration
public class DynamoDBStack extends Stack {
    public DynamoDBStack(@Nullable IamStack scope, @Nullable String id, @Nullable StackProps props) {
        super(scope, "DynamoDBStack", props);
    }

    @Bean
    public Table transcodingJobTable() {
        return Table.Builder.create(this, "transcoding-job-table")
                .tableName("TRANSCODING_JOB")
                .partitionKey(Attribute.builder()
                        .name("jobId")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PROVISIONED)
                .tableClass(TableClass.STANDARD)
                .readCapacity(1)
                .writeCapacity(1)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
    }
}
