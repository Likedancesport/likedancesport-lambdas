package com.likedancesport.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.cloudfront.BehaviorOptions;
import software.amazon.awscdk.services.cloudfront.Distribution;
import software.amazon.awscdk.services.cloudfront.PriceClass;
import software.amazon.awscdk.services.cloudfront.origins.S3Origin;
import software.amazon.awscdk.services.s3.IBucket;

@Configuration
public class CloudfrontConfig extends AbstractCdkConfig {
    @Autowired
    public CloudfrontConfig(Stack stack, StackProps stackProps) {
        super(stack, stackProps);
    }

    @Bean
    public Distribution distribution(@Qualifier("hlsBucket") IBucket hlsBucket) {
        return Distribution.Builder.create(stack, "likedancesport-hls-cdn")
                .priceClass(PriceClass.PRICE_CLASS_100)
                .defaultBehavior(BehaviorOptions.builder()
                        .origin(new S3Origin(hlsBucket))
                        .build())
                .build();
    }
}
