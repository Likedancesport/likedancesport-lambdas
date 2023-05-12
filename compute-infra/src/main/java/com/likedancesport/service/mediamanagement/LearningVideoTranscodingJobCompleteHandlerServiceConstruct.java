package com.likedancesport.service.mediamanagement;

import com.likedancesport.service.AbstractLambdaServiceConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.events.EventBus;
import software.amazon.awscdk.services.events.EventPattern;
import software.amazon.awscdk.services.events.Rule;
import software.amazon.awscdk.services.events.targets.SqsQueue;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.lambda.Alias;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Version;
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSource;
import software.amazon.awscdk.services.mediaconvert.CfnQueue;
import software.amazon.awscdk.services.s3.IBucket;
import software.amazon.awscdk.services.sqs.Queue;

import java.util.List;
import java.util.Map;

@Component
public class LearningVideoTranscodingJobCompleteHandlerServiceConstruct extends AbstractLambdaServiceConstruct {
    private final CfnQueue mediaConvertQueue;
    private final EventBus likedancesportEventBus;

    @Autowired
    public LearningVideoTranscodingJobCompleteHandlerServiceConstruct(IRole role,
                                                                      @Qualifier("codebaseBucket") IBucket codebaseBucket,
                                                                      LayerVersion commonLambdaLayer,
                                                                      @Qualifier("likedancesportLearningMediaConvertQueue") CfnQueue mediaConvertQueue,
                                                                      EventBus likedancesportEventBus) {
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

        Code code = Code.fromBucket(codebaseBucket, "learning-video-transcoding-job-complete-handler.jar");

        Function function = Function.Builder.create(stack, "learning-video-transcoding-job-complete-handler")
                .functionName("learning-video-transcoding-job-complete-handler")
                .architecture(Architecture.X86_64)
                .runtime(Runtime.JAVA_11)
                .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
                .code(code)
                .layers(List.of(commonLambdaLayer))
                .memorySize(3000)
                .build();

        Version version = function.getCurrentVersion();

        Alias alias = Alias.Builder.create(stack, "learning-video-transcoding-job-complete-handler-alias")
                .aliasName("learning-video-transcoding-job-complete-handler-alias")
                .version(version)
                .build();

        alias.addEventSource(new SqsEventSource(transcodingCompleteLambdaQueue));
    }
}
