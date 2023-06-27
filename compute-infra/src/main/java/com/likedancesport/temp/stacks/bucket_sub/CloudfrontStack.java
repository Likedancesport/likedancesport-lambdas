package com.likedancesport.temp.stacks.bucket_sub;

import com.likedancesport.temp.stacks.BucketsStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.NestedStack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.cloudfront.BehaviorOptions;
import software.amazon.awscdk.services.cloudfront.Distribution;
import software.amazon.awscdk.services.cloudfront.PriceClass;
import software.amazon.awscdk.services.cloudfront.origins.S3Origin;
import software.amazon.awscdk.services.s3.IBucket;

@Configuration
public class CloudfrontStack extends NestedStack {
    @Autowired
    public CloudfrontStack(BucketsStack stack, StackProps stackProps) {
        super(stack, "CloudfrontStack");
    }

    @Bean
    public Distribution distribution(@Qualifier("hlsBucket") IBucket hlsBucket) {
        return Distribution.Builder.create(this, "likedancesport-hls-cdn")
                .priceClass(PriceClass.PRICE_CLASS_100)
                .defaultBehavior(BehaviorOptions.builder()
                        .origin(new S3Origin(hlsBucket))
                        .build())
                .build();
    }
}
