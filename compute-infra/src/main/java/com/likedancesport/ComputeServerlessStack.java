package com.likedancesport;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.AuthorizationType;
import software.amazon.awscdk.services.apigateway.Integration;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.Method;
import software.amazon.awscdk.services.apigateway.MethodOptions;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.cloudfront.BehaviorOptions;
import software.amazon.awscdk.services.cloudfront.Distribution;
import software.amazon.awscdk.services.cloudfront.PriceClass;
import software.amazon.awscdk.services.cloudfront.origins.S3Origin;
import software.amazon.awscdk.services.events.EventBus;
import software.amazon.awscdk.services.events.EventPattern;
import software.amazon.awscdk.services.events.IEventBus;
import software.amazon.awscdk.services.events.IRuleTarget;
import software.amazon.awscdk.services.events.Rule;
import software.amazon.awscdk.services.events.targets.SnsTopic;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.lambda.Alias;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.CfnFunction;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.HttpMethod;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Version;
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSource;
import software.amazon.awscdk.services.mediaconvert.CfnQueue;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketAccessControl;
import software.amazon.awscdk.services.s3.BucketEncryption;
import software.amazon.awscdk.services.s3.EventType;
import software.amazon.awscdk.services.s3.IBucket;
import software.amazon.awscdk.services.s3.LifecycleRule;
import software.amazon.awscdk.services.s3.StorageClass;
import software.amazon.awscdk.services.s3.Transition;
import software.amazon.awscdk.services.s3.notifications.SnsDestination;
import software.amazon.awscdk.services.sns.ITopicSubscription;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.Queue;
import software.constructs.Construct;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.likedancesport.util.DevOpsConstants.*;

public class ComputeServerlessStack extends Stack {
    private Bucket mp4Bucket;
    private Bucket thumbnailsBucket;
    private Bucket hlsBucket;

    private final CfnQueue mediaConvertQueue;

    private final LayerVersion commonLayer;

    private final IRole role = Role.fromRoleArn(this, "lambda-role", "arn:aws:iam::066002146890:role/Rds-S3-SSM-Role");
    private final IBucket codebaseBucket = Bucket.fromBucketArn(this, "likedancesport-codebase", "arn:aws:s3:::likedancesport-codebase");


    public ComputeServerlessStack(final Construct scope, final String id, @NotNull final StackProps props) {
        super(scope, id, props);

        final IRole role = Role.fromRoleArn(this, "lambda-role", "arn:aws:iam::066002146890:role/Rds-S3-SSM-Role");
        final IBucket codebaseBucket = Bucket.fromBucketArn(this, "likedancesport-codebase", "arn:aws:s3:::likedancesport-codebase");

        initBuckets();

        configureMp4UploadTriggering();

        final Distribution hlsDistribution = Distribution.Builder.create(this, "likedancesport-hls-cdn")
                .priceClass(PriceClass.PRICE_CLASS_100)
                .defaultBehavior(BehaviorOptions.builder()
                        .origin(new S3Origin(hlsBucket))
                        .build())
                .build();

        final Code commonLambdaLayerCode = Code.fromBucket(codebaseBucket, "likedancesport-layer-dependencies.jar");

        commonLayer = LayerVersion.Builder.create(this, "likedancesport-common-lambda-layer")
                .layerVersionName("likedancesport-common-lambda-layer")
                .compatibleArchitectures(List.of(Architecture.X86_64, Architecture.ARM_64))
                .code(commonLambdaLayerCode)
                .compatibleRuntimes(List.of(Runtime.JAVA_11))
                .description("Base layer with common dependencies")
                .build();


        mediaConvertQueue = CfnQueue.Builder.create(this, "likedancesport-transcoding-queue")
                .name("likedancesport-transcoding-queue")
                .pricingPlan("ON_DEMAND")
                .status("ACTIVE")
                .build();

        final IEventBus likedancesportEventBus = EventBus.Builder.create(this, "likedancesport-event-bus")
                .eventBusName("likedancesport-event-bus")
                .build();

        final EventPattern mediaConvertJobCompleteEventPattern = EventPattern.builder()
                .source(List.of("aws.mediaconvert"))
                .detailType(List.of("MediaConvert Job State Change"))
                .region(List.of(props.getEnv().getRegion()))
                .account(List.of(props.getEnv().getAccount()))
                .detail(Map.of("status", "COMPLETE"
                        , "queue", mediaConvertQueue.getAttrArn()))
                .build();

//        IRuleTarget mediaConvertSnsTarget = SnsTopic.Builder;
        Rule.Builder.create(this, "media-convert-job-complete-event")
                .ruleName("media-convert-job-complete-event")
                .eventPattern(mediaConvertJobCompleteEventPattern)
                .eventBus(likedancesportEventBus);


    }

    private void configureMp4UploadTriggering() {
        final Topic mp4UploadTopic = Topic.Builder.create(this, "mp4-upload-topic")
                .topicName("mp4-upload-topic")
                .fifo(false)
                .build();

        final Queue mp4UploadLambdaQueue = Queue.Builder.create(this, "mp4-upload-lambda-queue")
                .queueName("mp4-upload-lambda-queue")
                .fifo(false)
                .visibilityTimeout(Duration.seconds(10))
                .build();

        final ITopicSubscription mp4UploadTopicSubscription = SqsSubscription.Builder.create(mp4UploadLambdaQueue).build();

        mp4UploadTopic.addSubscription(mp4UploadTopicSubscription);

        mp4Bucket.addEventNotification(EventType.OBJECT_CREATED, new SnsDestination(mp4UploadTopic));
    }

    private void initBuckets() {
        Transition mp4VideoTransition = Transition.builder()
                .storageClass(StorageClass.GLACIER)
                .transitionAfter(Duration.days(2))
                .build();

        LifecycleRule mp4VideoLifecycleRule = LifecycleRule.builder()
                .id("mp4-video-lifecycle-rule")
                .enabled(true)
                .transitions(List.of(mp4VideoTransition))
                .build();

        mp4Bucket = Bucket.Builder.create(this, "likedancesport-mp4-assets")
                .bucketName("likedancesport-mp4-assets")
                .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
                .accessControl(BucketAccessControl.BUCKET_OWNER_FULL_CONTROL)
                .eventBridgeEnabled(true)
                .lifecycleRules(List.of(mp4VideoLifecycleRule))
                .encryption(BucketEncryption.KMS_MANAGED)
                .build();

        thumbnailsBucket = Bucket.Builder.create(this, "likedancesport-thumbnails-bucket")
                .bucketName("likedancesport-thumbnails-bucket")
                .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
                .accessControl(BucketAccessControl.BUCKET_OWNER_FULL_CONTROL)
                .build();


        hlsBucket = Bucket.Builder.create(this, "likedancesport-hls-bucket")
                .bucketName("likedancesport-hls-bucket")
                .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
                .accessControl(BucketAccessControl.AUTHENTICATED_READ)
                .build();
    }

    private void configureMediaManagementLambda() {
        final Code mediaManagementLambdaCode = Code.fromBucket(codebaseBucket, "likedancesport-media-management-lambda-1.0.jar");

        final Function mediaManagementLambda = Function.Builder.create(this, "media-management-lambda")
                .architecture(Architecture.X86_64)
                .runtime(Runtime.JAVA_11)
                .memorySize(3000)
                .role(role)
                .layers(List.of(commonLayer))
                .functionName("media-management-lambda")
                .handler("com.likedancesport.MediaManagementLambdaHandler::handleRequest")
                .code(mediaManagementLambdaCode)
                .build();

        setSnapStart(mediaManagementLambda);

        final Version mediaManagementLambdaVersion = mediaManagementLambda.getCurrentVersion();

        final Alias mediaManagementAlias = Alias.Builder.create(this, "media-management-alias")
                .aliasName("snap-m-alias")
                .version(mediaManagementLambdaVersion)
                .build();

        final LambdaIntegration mediaManagementLambdaIntegration = LambdaIntegration.Builder.create(mediaManagementAlias)
                .requestTemplates(new HashMap<>() {{
                    put("application/json", "{ \"statusCode\": \"200\" }");
                }})
                .timeout(Duration.seconds(29))
                .proxy(true)
                .build();

        configureMediaManagementApi(mediaManagementLambdaIntegration);
    }

    private void configureMediaManagementApi(LambdaIntegration mediaManagementLambdaIntegration) {
        final RestApi likedancesportApi = RestApi.Builder.create(this, "likedancesport-management-api")
                .restApiName("likedancesport-management-api")
                .deploy(true)
                .deployOptions(StageOptions.builder()
                        .stageName("dev")
                        .build())
                .defaultMethodOptions(MethodOptions.builder()
                        .authorizationType(AuthorizationType.NONE)
                        .build())
                .build();

        final MethodOptions defaultMethodOptions = MethodOptions.builder()
                .authorizationType(AuthorizationType.NONE)
                .apiKeyRequired(false)
                .build();

        final Resource apiResource = likedancesportApi.getRoot().addResource("api");

        final Resource courses = apiResource.addResource("courses");

        final Method addCourseMethod = courses.addMethod(POST, mediaManagementLambdaIntegration, defaultMethodOptions);

        final Method getCoursesMethod = courses.addMethod(GET, mediaManagementLambdaIntegration, MethodOptions.builder()
                .requestParameters(new HashMap<>() {{
                    put("method.request.querystring.pageNumber", true);
                    put("method.request.querystring.pageSize", true);
                }})
                .apiKeyRequired(false)
                .build());

        final Resource course = courses.addResource("{courseId}");
        final Method getCourseMethod = course.addMethod(GET, mediaManagementLambdaIntegration, defaultMethodOptions);
        final Method updateCourseMethod = course.addMethod(PUT, mediaManagementLambdaIntegration, defaultMethodOptions);
        final Method deleteCourseMethod = course.addMethod(DELETE, mediaManagementLambdaIntegration, defaultMethodOptions);

        final Resource sections = course.addResource("sections");
        final Method createSectionMethod = sections.addMethod(POST, mediaManagementLambdaIntegration, defaultMethodOptions);

        final Resource section = sections.addResource("{sectionId}");
        final Method getSectionMethod = section.addMethod(GET, mediaManagementLambdaIntegration, defaultMethodOptions);
        final Method updateSectionMethod = section.addMethod(PUT, mediaManagementLambdaIntegration, defaultMethodOptions);
        final Method deleteSectionMethod = section.addMethod(DELETE, mediaManagementLambdaIntegration, defaultMethodOptions);

        final Resource videos = section.addResource("videos");
        final Method createVideoMethod = videos.addMethod(POST, mediaManagementLambdaIntegration, defaultMethodOptions);

        final Resource video = videos.addResource("{videoId}");
        final Method getVideoMethod = video.addMethod(GET, mediaManagementLambdaIntegration, defaultMethodOptions);
        final Method updateVideoMethod = video.addMethod(PUT, mediaManagementLambdaIntegration, defaultMethodOptions);
        final Method deleteVideoMethod = video.addMethod(DELETE, mediaManagementLambdaIntegration, defaultMethodOptions);
    }


    private void configureMp4VideoUploadHandlerLambda() {
        final Code videoUploadHandlerCode = Code.fromBucket(codebaseBucket, "mp4-video-upload-handler-1.0.jar");

        final Function videoUploadHandlerLambda = Function.Builder.create(this, "mp4-video-upload-handler")
                .architecture(Architecture.X86_64)
                .runtime(Runtime.JAVA_11)
                .memorySize(3000)
                .role(role)
                .layers(List.of(commonLayer))
                .functionName("video-upload-handler")
                .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
                .code(videoUploadHandlerCode)
                .build();


        setSnapStart(videoUploadHandlerLambda);

        final Version videoUploadHandlerLambdaVersion = videoUploadHandlerLambda.getCurrentVersion();

        final Alias videoUploadHandlerAlias = Alias.Builder.create(this, "mp4-video-upload-alias")
                .aliasName("snap-v-alias")
                .version(videoUploadHandlerLambdaVersion)
                .build();

        videoUploadHandlerAlias.addEventSource(SqsEventSource.Builder.create());
    }


    private void setSnapStart(IFunction lambda) {
        CfnFunction.SnapStartProperty snapStartProperty = CfnFunction.SnapStartProperty.builder()
                .applyOn("PublishedVersions")
                .build();
        ((CfnFunction) lambda.getNode().getDefaultChild()).setSnapStart(snapStartProperty);
    }

    // MediaConvert Job Template
    /*        String pathToJobTemplateJson = System.getProperty("user.dir") + "/src/main/resources/JobTemplateSettings.json";
        System.out.println(pathToJobTemplateJson);
        JsonNode jsonNode;

        try {
            jsonNode = new ObjectMapper().readValue(new File(pathToJobTemplateJson), JsonNode.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final CfnJobTemplate mediaConvertJobTemplate = CfnJobTemplate.Builder.create(this, "media-convert-classic-job-template")
                .settingsJson(jsonNode)
                .name("media-convert-classic-job-template")
                .build();*/

}
