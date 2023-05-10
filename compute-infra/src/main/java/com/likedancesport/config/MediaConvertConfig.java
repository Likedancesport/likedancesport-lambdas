package com.likedancesport.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.mediaconvert.CfnQueue;

@Configuration
public class MediaConvertConfig extends AbstractCdkConfig{
    @Autowired
    public MediaConvertConfig(Stack stack, StackProps stackProps) {
        super(stack, stackProps);
    }

    @Bean(name = "likedancesportLearningMediaConvertQueue")
    public CfnQueue likedancesportLearningMediaConvertQueue() {
        return CfnQueue.Builder.create(stack, "likedancesport-learning-transcoding-queue")
                .name("likedancesport-learning-transcoding-queue")
                .pricingPlan("ON_DEMAND")
                .status("ACTIVE")
                .build();
    }

    @Bean(name = "likedancesportMarketplaceMediaConvertQueue")
    public CfnQueue likedancesportMarketplaceMediaConvertQueue() {
        return CfnQueue.Builder.create(stack, "likedancesport-marketplace-transcoding-queue")
                .name("likedancesport-marketplace-transcoding-queue")
                .pricingPlan("ON_DEMAND")
                .status("ACTIVE")
                .build();
    }
}
