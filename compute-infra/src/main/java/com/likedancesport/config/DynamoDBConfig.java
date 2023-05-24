package com.likedancesport.config;

import com.likedancesport.ComputeStack;
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

@Configuration
public class DynamoDBConfig extends AbstractCdkConfig{
    public DynamoDBConfig(ComputeStack stack, StackProps stackProps) {
        super(stack, stackProps);
    }

    @Bean
    public Table transcodingJobTable() {
        return Table.Builder.create(stack, "transcoding-job-table")
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
