package com.likedancesport.temp.service;

import com.likedancesport.temp.stacks.bucket_sub.eventbridge_sub.ComputeStack;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import software.amazon.awscdk.Duration;
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
public class LearningVideoUploadHandlerServiceStack extends AbstractLambdaServiceStack {
    private final Bucket mp4AssetsBucket;

    public LearningVideoUploadHandlerServiceStack(@NotNull ComputeStack scope, IRole role,
                                                  IBucket codebaseBucket, LayerVersion commonLambdaLayer,
                                                  Bucket mp4AssetsBucket) {
        super(scope, "LearningVideoUploadHandlerServiceStack", role, codebaseBucket, commonLambdaLayer);
        this.mp4AssetsBucket = mp4AssetsBucket;
    }

    @Override
    public void construct() {
        Code code = Code.fromBucket(codebaseBucket, "learning-video-upload-handler.jar");

        String functionName = "learning-video-upload-handler";

        IFunction function = buildSpringCloudFunctionLambda(this, code, functionName);
//TODO: try to implement with SQS

        Queue queue = Queue.Builder.create(this, "learning-asset-upload-handler-queue")
                .visibilityTimeout(Duration.seconds(10))
                .retentionPeriod(Duration.hours(5))
                .queueName("learning-asset-upload-handler-queue")
                .build();

        mp4AssetsBucket.addEventNotification(EventType.OBJECT_CREATED, new SqsDestination(queue));

        function.addEventSource(new SqsEventSource(queue));
    }
}
