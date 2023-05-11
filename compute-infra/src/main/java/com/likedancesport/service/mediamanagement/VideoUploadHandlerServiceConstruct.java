package com.likedancesport.service.mediamanagement;

import com.likedancesport.service.AbstractLambdaServiceConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.lambda.Alias;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Version;
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSource;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.EventType;
import software.amazon.awscdk.services.s3.IBucket;
import software.amazon.awscdk.services.s3.notifications.SqsDestination;
import software.amazon.awscdk.services.sqs.Queue;

import java.util.List;

@Component
public class VideoUploadHandlerServiceConstruct extends AbstractLambdaServiceConstruct {
    private final Bucket mp4AssetsBucket;

    @Autowired
    public VideoUploadHandlerServiceConstruct(IRole role,
                                              @Qualifier("codebaseBucket") IBucket codebaseBucket,
                                              LayerVersion commonLambdaLayer,
                                              @Qualifier("mp4AssetsBucket") Bucket mp4AssetsBucket) {
        super(role, codebaseBucket, commonLambdaLayer);
        this.mp4AssetsBucket = mp4AssetsBucket;
    }

    @Override
    public void construct(Stack stack, StackProps stackProps) {
        Code code = Code.fromBucket(codebaseBucket, "mp4-video-upload-handler-1.0.jar");

        Function function = Function.Builder.create(stack, "mp4-video-upload-handler")
                .architecture(Architecture.X86_64)
                .runtime(Runtime.JAVA_11)
                .memorySize(3000)
                .role(role)
                .layers(List.of(commonLambdaLayer))
                .functionName("video-upload-handler")
                .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
                .code(code)
                .build();

        Version version = function.getCurrentVersion();

        Alias alias = Alias.Builder.create(stack, "video-upload-handler-alias")
                .aliasName("snap-v-alias")
                .version(version)
                .build();

        Queue queue = Queue.Builder.create(stack, "mp4-asset-upload-handler-queue")
                .visibilityTimeout(Duration.seconds(10))
                .retentionPeriod(Duration.hours(5))
                .fifo(false)
                .queueName("mp4-asset-upload-handler-queue")
                .build();

        mp4AssetsBucket.addEventNotification(EventType.OBJECT_CREATED, new SqsDestination(queue));

        alias.addEventSource(new SqsEventSource(queue));
    }
}
