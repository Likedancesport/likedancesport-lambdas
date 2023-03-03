package com.likedancesport.common.config;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


@Configuration
public class SsmConfig {
    @Bean
    @Order(value = Ordered.LOWEST_PRECEDENCE)
    public AWSSimpleSystemsManagement ssm() {
        return AWSSimpleSystemsManagementClientBuilder.defaultClient();
    }
}
