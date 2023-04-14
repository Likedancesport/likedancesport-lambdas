package com.likedancesport;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.*;
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

import java.util.HashMap;
import java.util.List;

import static com.likedancesport.util.DevOpsConstants.*;

public class ComputeServerlessStack extends Stack {

    public ComputeServerlessStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public ComputeServerlessStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        final IRole role = Role.fromRoleArn(this, "lambda-role", "arn:aws:iam::066002146890:role/Rds-S3-SSM-Role");
        final IBucket deploymentBucket = Bucket.fromBucketArn(this, "likedancesport-codebase", "arn:aws:s3:::likedancesport-codebase");


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

        final Distribution hlsDistribution = Distribution.Builder.create(this, "likedancesport-hls-cdn")
                .priceClass(PriceClass.PRICE_CLASS_100)
                .defaultBehavior(BehaviorOptions.builder()
                        .origin(new S3Origin(hlsBucket))
                        .build())
                .build();

        final Code commonLambdaLayerCode = Code.fromBucket(deploymentBucket, "likedancesport-layer-dependencies.jar");

        final LayerVersion commonLayer = LayerVersion.Builder.create(this, "likedancesport-common-lambda-layer")
                .layerVersionName("likedancesport-common-lambda-layer")
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
                .handler("com.likedancesport.MediaManagementLambdaHandler::handleRequest")
                .code(mediaManagementLambdaCode)
                .build();

        final Code videoUploadHandlerCode = Code.fromBucket(deploymentBucket, "video-upload-handler-1.0.jar");

        final IEventSource mp4s3UploadEventSource = S3EventSource.Builder.create(mp4Bucket)
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


        final Version mediaManagementLambdaCurrentVersion = Version.Builder.create(this, "media-management-v")
                .lambda(mediaManagementLambda)
                .build();

        final Version videoUploadHandlerLambdaCurrentVersion = Version.Builder.create(this, "video-upload-handler-v")
                .lambda(videoUploadHandlerLambda)
                .build();

        final Alias mediaManagementAlias = Alias.Builder.create(this, "media-management-alias")
                .aliasName("snap-m-alias")
                .version(mediaManagementLambdaCurrentVersion)
                .build();

        final Alias videoUploadHandlerAlias = Alias.Builder.create(this, "video-upload-alias")
                .aliasName("snap-v-alias")
                .version(videoUploadHandlerLambdaCurrentVersion)
                .build();


        final RestApi likedancesportApi = RestApi.Builder.create(this, "likedancesport-management-api")
                .restApiName("likedancesport-management-api")
                .build();

        final LambdaIntegration mediaManagementLambdaIntegration = LambdaIntegration.Builder.create(mediaManagementAlias)
                .requestTemplates(new HashMap<>() {{
                    put("application/json", "{ \"statusCode\": \"200\" }");
                }})
                .proxy(true)
                .build();

        final Resource apiResource = likedancesportApi.getRoot().addResource("api");

        final Resource courses = apiResource.addResource("courses");
        final Method addCourseMethod = courses.addMethod(POST, mediaManagementLambdaIntegration);

        final Method getCoursesMethod = courses.addMethod(GET, mediaManagementLambdaIntegration, MethodOptions.builder()
                .requestParameters(new HashMap<>() {{
                    put("method.request.querystring.pageNumber", true);
                    put("method.request.querystring.pageSize", true);
                }})
                .build());

        final Resource course = courses.addResource("{courseId}");
        final Method getCourseMethod = course.addMethod(GET, mediaManagementLambdaIntegration);
        final Method updateCourseMethod = course.addMethod(PUT, mediaManagementLambdaIntegration);
        final Method deleteCourseMethod = course.addMethod(DELETE, mediaManagementLambdaIntegration);

        final Resource sections = courses.addResource("sections");
        final Method createSectionMethod = sections.addMethod(POST, mediaManagementLambdaIntegration);

        final Resource section = sections.addResource("{sectionId}");
        final Method getSectionMethod = section.addMethod(GET, mediaManagementLambdaIntegration);
        final Method updateSectionMethod = section.addMethod(PUT, mediaManagementLambdaIntegration);
        final Method deleteSectionMethod = section.addMethod(DELETE, mediaManagementLambdaIntegration);

        final Resource videos = section.addResource("videos");
        final Method createVideoMethod = videos.addMethod(POST, mediaManagementLambdaIntegration);

        final Resource video = videos.addResource("{video}");
        final Method getVideoMethod = video.addMethod(GET, mediaManagementLambdaIntegration);
        final Method updateVideoMethod = video.addMethod(PUT, mediaManagementLambdaIntegration);
        final Method deleteVideoMethod = video.addMethod(DELETE, mediaManagementLambdaIntegration);

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
