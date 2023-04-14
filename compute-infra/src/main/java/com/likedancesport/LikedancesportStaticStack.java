package com.likedancesport;

import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.cloudfront.BehaviorOptions;
import software.amazon.awscdk.services.cloudfront.Distribution;
import software.amazon.awscdk.services.cloudfront.PriceClass;
import software.amazon.awscdk.services.cloudfront.origins.S3Origin;
import software.amazon.awscdk.services.s3.*;
import software.constructs.Construct;

import java.util.List;

public class LikedancesportStaticStack extends Stack {
    public LikedancesportStaticStack(@Nullable Construct scope, @Nullable String id, @Nullable StackProps props) {
        super(scope, id, props);

        /*final Transition mp4VideoTransition = Transition.builder()
                .storageClass(StorageClass.GLACIER)
                .transitionAfter(Duration.days(2))
                .build();

        final LifecycleRule mp4VideoLifecycleRule = LifecycleRule.builder()
                .id("mp4-video-lifecycle-rule")
                .enabled(true)
                .transitions(List.of(mp4VideoTransition))
                .build();

        final Bucket mp4Bucket = Bucket.Builder.create(this, "likedancesport-mp4-assets")
                .bucketName("likedancesport-mp4-assets")
                .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
                .accessControl(BucketAccessControl.BUCKET_OWNER_FULL_CONTROL)
                .eventBridgeEnabled(true)
                .lifecycleRules(List.of(mp4VideoLifecycleRule))
                .encryption(BucketEncryption.KMS_MANAGED)
                .build();


        final Bucket thumbnailsBucket = Bucket.Builder.create(this, "likedancesport-thumbnails-bucket")
                .bucketName("likedancesport-thumbnails-bucket")
                .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
                .accessControl(BucketAccessControl.BUCKET_OWNER_FULL_CONTROL)
                .build();


        final Bucket hlsBucket = Bucket.Builder.create(this, "likedancesport-hls-bucket")
                .bucketName("likedancesport-hls-bucket")
                .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
                .accessControl(BucketAccessControl.AUTHENTICATED_READ)
                .build();

        Distribution hlsDistribution = Distribution.Builder.create(this, "likedancesport-hls-cdn")
                .priceClass(PriceClass.PRICE_CLASS_100)
                .defaultBehavior(BehaviorOptions.builder()
                        .origin(new S3Origin(hlsBucket))
                        .build())
                .build();
*/
    }

    public LikedancesportStaticStack(@Nullable Construct scope, @Nullable String id) {
        this(scope, id, null);
    }
}
