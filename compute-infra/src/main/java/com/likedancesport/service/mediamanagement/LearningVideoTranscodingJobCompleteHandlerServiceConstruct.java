package com.likedancesport.service.mediamanagement;

import com.likedancesport.service.AbstractLambdaServiceConstruct;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.events.EventBus;
import software.amazon.awscdk.services.events.EventPattern;
import software.amazon.awscdk.services.events.Rule;
import software.amazon.awscdk.services.events.targets.SqsQueue;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.mediaconvert.CfnQueue;
import software.amazon.awscdk.services.s3.IBucket;
import software.amazon.awscdk.services.sqs.Queue;

import java.util.List;
import java.util.Map;

public class LearningVideoTranscodingJobCompleteHandlerServiceConstruct extends AbstractLambdaServiceConstruct {
    private final CfnQueue mediaConvertQueue;
    private final EventBus likedancesportEventBus;

    public LearningVideoTranscodingJobCompleteHandlerServiceConstruct(IRole role, IBucket codebaseBucket, LayerVersion commonLambdaLayer, CfnQueue mediaConvertQueue, EventBus likedancesportEventBus) {
        super(role, codebaseBucket, commonLambdaLayer);
        this.mediaConvertQueue = mediaConvertQueue;
        this.likedancesportEventBus = likedancesportEventBus;
    }

    @Override
    public void construct(Stack stack, StackProps stackProps) {
        Queue transcodingCompleteLambdaQueue = Queue.Builder.create(stack, "learning-video-transcoding-job-complete-handler-queue")
                .queueName("learning-video-transcoding-job-complete-handler-queue")
                .fifo(false)
                .visibilityTimeout(Duration.seconds(10))
                .build();

        EventPattern eventPattern = EventPattern.builder()
                .source(List.of("aws.mediaconvert"))
                .detailType(List.of("MediaConvert Job State Change"))
                .region(List.of(stackProps.getEnv().getRegion()))
                .account(List.of(stackProps.getEnv().getAccount()))
                .detail(Map.of("status", "COMPLETE",
                        "queue", mediaConvertQueue.getAttrArn()))
                .build();

        Rule.Builder.create(stack, "media-convert-learning-job-complete-event")
                .ruleName("media-convert-learning-job-complete-event")
                .eventPattern(eventPattern)
                .targets(List.of(new SqsQueue(transcodingCompleteLambdaQueue)))
                .eventBus(likedancesportEventBus)
                .build();


    }
}
