package com.likedancesport.service;

import com.likedancesport.service.AbstractLambdaServiceConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.events.EventPattern;
import software.amazon.awscdk.services.events.IEventBus;
import software.amazon.awscdk.services.events.Rule;
import software.amazon.awscdk.services.events.targets.SqsQueue;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSource;
import software.amazon.awscdk.services.mediaconvert.CfnQueue;
import software.amazon.awscdk.services.s3.IBucket;
import software.amazon.awscdk.services.sqs.Queue;

import java.util.List;
import java.util.Map;

@Component
public class LearningVideoTranscodingJobCompleteHandlerServiceConstruct extends AbstractLambdaServiceConstruct {
    private final CfnQueue mediaConvertQueue;
    private final IEventBus likedancesportEventBus;

    @Autowired
    public LearningVideoTranscodingJobCompleteHandlerServiceConstruct(IRole role,
                                                                      @Qualifier("codebaseBucket") IBucket codebaseBucket,
                                                                      LayerVersion commonLambdaLayer,
                                                                      @Qualifier("likedancesportLearningMediaConvertQueue") CfnQueue mediaConvertQueue,
                                                                      IEventBus likedancesportEventBus) {
        super(role, codebaseBucket, commonLambdaLayer);
        this.mediaConvertQueue = mediaConvertQueue;
        this.likedancesportEventBus = likedancesportEventBus;
    }

    @Override
    public void construct(Stack stack, StackProps stackProps) {
        Queue transcodingCompleteLambdaQueue = Queue.Builder.create(stack, "learning-video-transcoding-job-complete-handler-queue")
                .queueName("learning-video-transcoding-job-complete-handler-queue")
                .visibilityTimeout(Duration.seconds(10))
                .retentionPeriod(Duration.hours(5))
                .build();

        EventPattern eventPattern = EventPattern.builder()
                .source(List.of("aws.mediaconvert"))
                .detailType(List.of("MediaConvert Job State Change"))
                .region(List.of(stackProps.getEnv().getRegion()))
                .account(List.of(stackProps.getEnv().getAccount()))
                .detail(Map.of("status", new String[]{"COMPLETE"},
                        "queue", new String[]{mediaConvertQueue.getAttrArn()}))
                .build();

        Rule.Builder.create(stack, "media-convert-learning-job-complete-event")
                .ruleName("media-convert-learning-job-complete-event")
                .eventPattern(eventPattern)
                .targets(List.of(new SqsQueue(transcodingCompleteLambdaQueue)))
                .eventBus(likedancesportEventBus)
                .build();

        Code code = Code.fromBucket(codebaseBucket, "learning-video-transcoding-job-complete-handler.jar");

        IFunction function = buildSpringCloudFunctionLambda(stack, code, "learning-video-transcoding-job-complete-handler");

        function.addEventSource(new SqsEventSource(transcodingCompleteLambdaQueue));
    }
}