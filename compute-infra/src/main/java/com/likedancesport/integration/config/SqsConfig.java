package com.likedancesport.integration.config;

import com.likedancesport.config.AbstractCdkConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.Queue;

@Configuration
public class SqsConfig extends AbstractCdkConfig {
    @Autowired
    public SqsConfig(Stack stack, StackProps stackProps) {
        super(stack, stackProps);
    }

    @Bean(name = "videoUploadLambdaQueue")
    public Queue videoUploadLambdaQueue(@Qualifier("videoUploadTopic") Topic videoUploadTopic) {
        return generateQueueForTopic("video-upload-lambda-queue", videoUploadTopic);
    }

    @Bean(name = "learningVideoTranscodingJobCompleteLambdaQueue")
    public Queue learningVideoTranscodingJobCompleteLambdaQueue(@Qualifier("learningVideoTranscodingJobCompleteTopic")
                                                                Topic topic) {
        return generateQueueForTopic("learning-video-transcoding-job-complete-lambda-queue", topic);
    }

    @NotNull
    private Queue generateQueueForTopic(String id, Topic topic) {
        Queue queue = Queue.Builder.create(stack, id)
                .queueName(id)
                .fifo(false)
                .visibilityTimeout(Duration.seconds(10))
                .build();

        SqsSubscription sqsSubscription = SqsSubscription.Builder.create(queue).build();
        topic.addSubscription(sqsSubscription);
        return queue;
    }
}
