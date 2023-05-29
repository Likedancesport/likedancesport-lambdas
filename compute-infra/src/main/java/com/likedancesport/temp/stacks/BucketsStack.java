package com.likedancesport.temp.stacks;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.NestedStack;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.CorsRule;
import software.amazon.awscdk.services.s3.HttpMethods;
import software.amazon.awscdk.services.s3.IBucket;
import software.amazon.awscdk.services.s3.LifecycleRule;
import software.amazon.awscdk.services.s3.StorageClass;
import software.amazon.awscdk.services.s3.Transition;
import software.constructs.Construct;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class BucketsStack extends NestedStack {
    private static final RemovalPolicy REMOVAL_POLICY = RemovalPolicy.DESTROY;

    public BucketsStack(@NotNull IamStack scope, @NotNull String id) {
        super(scope, "BucketsStack");
    }

    @Bean("codebaseBucket")
    public IBucket codebaseBucket() {
        return Bucket.fromBucketArn(this, "likedancesport-codebase", "arn:aws:s3:::likedancesport-codebase");
    }

    @Bean(name = "mp4AssetsBucket")
    public Bucket mp4Bucket() {
        Transition mp4VideoTransition = Transition.builder()
                .storageClass(StorageClass.GLACIER)
                .transitionAfter(Duration.days(2))
                .build();

        LifecycleRule mp4VideoLifecycleRule = LifecycleRule.builder()
                .id("mp4-video-lifecycle-rule")
                .enabled(true)
                .transitions(List.of(mp4VideoTransition))
                .build();

        return Bucket.Builder.create(this, "likedancesport-mp4-assets")
                .bucketName("likedancesport-mp4-assets")
                .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
                .publicReadAccess(false)
                .cors(new ArrayList<>() {{
                    add(CorsRule.builder()
                            .allowedMethods(List.of(HttpMethods.PUT))
                            .allowedOrigins(List.of("*"))
                            .allowedHeaders(List.of("*"))
                            .build());
                }})
                .eventBridgeEnabled(true)
                .removalPolicy(REMOVAL_POLICY)
                .lifecycleRules(List.of(mp4VideoLifecycleRule))
                .build();
    }

    @Bean(name = "hlsBucket")
    public Bucket hlsBucket() {
        return Bucket.Builder.create(this, "likedancesport-hls-bucket")
                .bucketName("likedancesport-hls-bucket")
                .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
                .removalPolicy(REMOVAL_POLICY)
                .build();
    }

    @Bean(name = "thumbnailsBucket")
    public Bucket thumbnailsBucket() {
        return Bucket.Builder.create(this, "likedancesport-thumbnails-bucket")
                .bucketName("likedancesport-thumbnails-bucket")
                .removalPolicy(REMOVAL_POLICY)
                .build();
    }
}
