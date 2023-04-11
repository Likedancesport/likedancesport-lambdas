package com.likedancesport;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.cloudfront.BehaviorOptions;
import software.amazon.awscdk.services.cloudfront.Distribution;
import software.amazon.awscdk.services.cloudfront.PriceClass;
import software.amazon.awscdk.services.cloudfront.origins.S3Origin;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.lambda.eventsources.S3EventSource;
import software.amazon.awscdk.services.s3.*;
import software.constructs.Construct;

import java.util.List;

public class ComputeServerlessStack extends Stack {
    public ComputeServerlessStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public ComputeServerlessStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        final IRole role = Role.fromRoleArn(this, "lambda-role", "arn:aws:iam::066002146890:role/Rds-S3-SSM-Role");
        IBucket deploymentBucket = Bucket.fromBucketArn(this, "likedancesport-codebase", "arn:aws:s3:::likedancesport-codebase");


        final Transition mp4VideoTransition = Transition.builder()
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


        final Code commonLambdaLayerCode = Code.fromBucket(deploymentBucket, "likedancesport-layer-dependencies.jar");

        final LayerVersion commonLayer = LayerVersion.Builder.create(this, "common-lambda-layer")
                .layerVersionName("common-lambda-layer")
                .compatibleArchitectures(List.of(Architecture.X86_64, Architecture.ARM_64))
                .code(commonLambdaLayerCode)
                .compatibleRuntimes(List.of(Runtime.JAVA_11))
                .description("Base layer with common dependencies")
                .build();

        final Code mediaManagementLambdaCode = Code.fromBucket(deploymentBucket, "likedancesport-media-management-lambda-1.0.jar");

        final Function mediaManagementLambda = Function.Builder.create(this, "media-management-lambda")
                .architecture(Architecture.X86_64)
                .runtime(Runtime.JAVA_11)
                .memorySize(2048)
                .role(role)
                .layers(List.of(commonLayer))
                .functionName("media-management-lambda")
                .handler("com.likedancesport.LambdaHandler:handleRequest")
                .code(mediaManagementLambdaCode)
                .build();

        final Code videoUploadHandlerCode = Code.fromBucket(deploymentBucket, "video-upload-handler-1.0.jar");

        IEventSource mp4s3UploadEventSource = S3EventSource.Builder.create(mp4Bucket)
                .events(List.of(EventType.OBJECT_CREATED))
                .build();

        final Function videoUploadHandlerLambda = Function.Builder.create(this, "video-upload-handler")
                .events(List.of(mp4s3UploadEventSource))
                .architecture(Architecture.X86_64)
                .runtime(Runtime.JAVA_11)
                .memorySize(2048)
                .role(role)
                .layers(List.of(commonLayer))
                .functionName("video-upload-handler")
                .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
                .code(videoUploadHandlerCode)
                .build();

        setSnapStart(mediaManagementLambda);
        setSnapStart(videoUploadHandlerLambda);

        final Version mediaManagementLambdaCurrentVersion = mediaManagementLambda.getCurrentVersion();

        final Version videoUploadHandlerLambdaCurrentVersion = videoUploadHandlerLambda.getCurrentVersion();

        final Alias mediaManagementAlias = Alias.Builder.create(this, "media-management-alias")
                .aliasName("snap-alias")
                .version(mediaManagementLambdaCurrentVersion)
                .build();

        final Alias videoUploadHandlerAlias = Alias.Builder.create(this, "video-upload-alias")
                .aliasName("snap-alias")
                .version(videoUploadHandlerLambdaCurrentVersion)
                .build();


    }

    private void setSnapStart(IFunction lambda) {
        CfnFunction.SnapStartProperty snapStartProperty = CfnFunction.SnapStartProperty.builder()
                .applyOn("PublishedVersions")
                .build();
        ((CfnFunction) lambda.getNode().getDefaultChild()).setSnapStart(snapStartProperty);
    }


}
