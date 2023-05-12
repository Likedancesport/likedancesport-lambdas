package com.likedancesport.service.mediamanagement;

import com.likedancesport.service.AbstractLambdaServiceConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSource;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.EventType;
import software.amazon.awscdk.services.s3.IBucket;
import software.amazon.awscdk.services.s3.notifications.SqsDestination;
import software.amazon.awscdk.services.sqs.Queue;

@Component
public class LearningVideoUploadHandlerServiceConstruct extends AbstractLambdaServiceConstruct {
    private final Bucket mp4AssetsBucket;

    @Autowired
    public LearningVideoUploadHandlerServiceConstruct(IRole role,
                                                      @Qualifier("codebaseBucket") IBucket codebaseBucket,
                                                      LayerVersion commonLambdaLayer,
                                                      @Qualifier("mp4AssetsBucket") Bucket mp4AssetsBucket) {
        super(role, codebaseBucket, commonLambdaLayer);
        this.mp4AssetsBucket = mp4AssetsBucket;
    }

    @Override
    public void construct(Stack stack, StackProps stackProps) {
        Code code = Code.fromBucket(codebaseBucket, "learning-video-upload-handler.jar");

        String functionName = "learning-video-upload-handler";

        IFunction function = buildSpringCloudFunctionLambda(stack, code, functionName);

        Queue queue = Queue.Builder.create(stack, "learning-asset-upload-handler-queue")
                .visibilityTimeout(Duration.seconds(10))
                .retentionPeriod(Duration.hours(5))
                .queueName("learning-asset-upload-handler-queue")
                .build();

        mp4AssetsBucket.addEventNotification(EventType.OBJECT_CREATED, new SqsDestination(queue));

        function.addEventSource(new SqsEventSource(queue));
    }
}
