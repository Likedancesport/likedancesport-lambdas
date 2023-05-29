package com.likedancesport.temp.stacks.bucket_sub.eventbridge_sub;

import com.likedancesport.temp.stacks.bucket_sub.EventBridgeStack;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.NestedStack;
import software.amazon.awscdk.services.mediaconvert.CfnQueue;

@Configuration
public class MediaConvertStack extends NestedStack {
    public MediaConvertStack(@NotNull EventBridgeStack scope, @NotNull String id) {
        super(scope, "MediaConvertStack");
    }

    @Bean(name = "likedancesportLearningMediaConvertQueue")
    public CfnQueue likedancesportLearningMediaConvertQueue() {
        return CfnQueue.Builder.create(this, "likedancesport-learning-transcoding-queue")
                .name("likedancesport-learning-transcoding-queue")
                .pricingPlan("ON_DEMAND")
                .status("ACTIVE")
                .build();
    }
}
