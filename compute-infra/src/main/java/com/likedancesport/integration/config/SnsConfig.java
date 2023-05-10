package com.likedancesport.integration.config;

import com.likedancesport.config.AbstractCdkConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.events.Rule;
import software.amazon.awscdk.services.events.targets.SnsTopic;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.EventType;
import software.amazon.awscdk.services.s3.notifications.SnsDestination;
import software.amazon.awscdk.services.sns.Topic;

@Configuration
public class SnsConfig extends AbstractCdkConfig {

    @Autowired
    public SnsConfig(Stack stack, StackProps stackProps) {
        super(stack, stackProps);
    }

    @Bean(name = "videoUploadTopic")
    public Topic videoUploadTopic(@Qualifier("mp4Bucket") Bucket mp4Bucket) {
        Topic videoUploadTopic = Topic.Builder.create(stack, "video-upload-topic")
                .topicName("video-upload-topic")
                .fifo(false)
                .build();

        mp4Bucket.addEventNotification(EventType.OBJECT_CREATED, new SnsDestination(videoUploadTopic));
        return videoUploadTopic;
    }

    @Bean(name = "learningVideoTranscodingJobCompleteTopic")
    public Topic learningVideoTranscodingJobCompleteTopic(@Qualifier("mediaConvertLearningVideoTranscodingCompleteEventRule")
                                                          Rule mediaConvertLearningVideoTranscodingCompleteEventRule) {
        Topic topic = Topic.Builder.create(stack, "learning-video-transcoding-job-complete-topic")
                .topicName("learning-video-transcoding-job-complete-topic")
                .fifo(false)
                .build();
        mediaConvertLearningVideoTranscodingCompleteEventRule.addTarget(new SnsTopic(topic));
        return topic;
    }
}
