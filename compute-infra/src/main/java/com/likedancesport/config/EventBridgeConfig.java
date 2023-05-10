package com.likedancesport.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.events.EventBus;
import software.amazon.awscdk.services.events.EventPattern;
import software.amazon.awscdk.services.events.IEventBus;
import software.amazon.awscdk.services.events.Rule;
import software.amazon.awscdk.services.mediaconvert.CfnQueue;

import java.util.List;
import java.util.Map;

@Configuration
public class EventBridgeConfig extends AbstractCdkConfig {
    @Autowired
    public EventBridgeConfig(Stack stack, StackProps stackProps) {
        super(stack, stackProps);
    }

    @Bean(name = "likedancesportEventBus")
    public IEventBus likedancesportEventBus() {
        return EventBus.Builder.create(stack, "likedancesport-event-bus")
                .eventBusName("likedancesport-event-bus")
                .build();
    }

    @Bean(name = "mediaConvertLearningVideoTranscodingCompleteEventPattern")
    public EventPattern mediaConvertLearningVideoTranscodingCompleteEventPattern(@Qualifier("likedancesportLearningMediaConvertQueue")
                                                                    CfnQueue likedancesportLearningMediaConvertQueue) {
        return getEventPatternForQueue(likedancesportLearningMediaConvertQueue);
    }

    @Bean(name = "mediaConvertMarketplaceThumbnailGenEventPattern")
    public EventPattern mediaConvertMarketplaceThumbnailGenEventPattern(@Qualifier("likedancesportMarketplaceMediaConvertQueue")
                                                                        CfnQueue likedancesportMarketplaceMediaConvertQueue) {
        return getEventPatternForQueue(likedancesportMarketplaceMediaConvertQueue);
    }

    @Bean(name = "mediaConvertLearningVideoTranscodingCompleteEventRule")
    public Rule mediaConvertLearningVideoTranscodingCompleteEventRule(@Qualifier("mediaConvertLearningVideoTranscodingCompleteEventPattern")
                                                         EventPattern mediaConvertLearningVideoTranscodingCompleteEventPattern) {
        return Rule.Builder.create(stack, "media-convert-learning-job-complete-event")
                .ruleName("media-convert-job-complete-event")
                .eventPattern(mediaConvertLearningVideoTranscodingCompleteEventPattern)
                .eventBus(likedancesportEventBus())
                .build();
    }

    @NotNull
    private EventPattern getEventPatternForQueue(CfnQueue cfnQueue) {
        return EventPattern.builder()
                .source(List.of("aws.mediaconvert"))
                .detailType(List.of("MediaConvert Job State Change"))
                .region(List.of(stackProps.getEnv().getRegion()))
                .account(List.of(stackProps.getEnv().getAccount()))
                .detail(Map.of("status", "COMPLETE",
                        "queue", cfnQueue.getAttrArn()))
                .build();
    }
}
