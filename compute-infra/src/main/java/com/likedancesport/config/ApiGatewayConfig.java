package com.likedancesport.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.AuthorizationType;
import software.amazon.awscdk.services.apigateway.MethodOptions;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.apigateway.StageOptions;

@Configuration
public class ApiGatewayConfig extends AbstractCdkConfig {
    @Autowired
    public ApiGatewayConfig(Stack stack, StackProps stackProps) {
        super(stack, stackProps);
    }

    @Bean(name = "mediaManagementApi")
    public RestApi mediaManagementApi() {
        return RestApi.Builder.create(stack, "likedancesport-management-api")
                .restApiName("likedancesport-management-api")
                .deploy(true)
                .deployOptions(StageOptions.builder()
                        .stageName("dev")
                        .build())
                .defaultMethodOptions(MethodOptions.builder()
                        .authorizationType(AuthorizationType.NONE)
                        .build())
                .build();
    }
}
