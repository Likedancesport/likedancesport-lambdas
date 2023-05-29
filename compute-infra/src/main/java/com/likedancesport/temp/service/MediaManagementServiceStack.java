package com.likedancesport.temp.service;

import com.likedancesport.temp.stacks.bucket_sub.eventbridge_sub.ComputeStack;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.apigateway.AuthorizationType;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.Method;
import software.amazon.awscdk.services.apigateway.MethodOptions;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.HttpMethod;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.s3.IBucket;

import java.util.HashMap;

@Component
public class MediaManagementServiceStack extends AbstractLambdaServiceStack {
    public static final String POST = HttpMethod.POST.name();
    public static final String DELETE = HttpMethod.DELETE.name();
    public static final String PUT = HttpMethod.PUT.name();
    public static final String GET = HttpMethod.GET.name();

    public MediaManagementServiceStack(@NotNull ComputeStack scope, @NotNull String id, IRole role, IBucket codebaseBucket, LayerVersion commonLambdaLayer) {
        super(scope, "MediaManagementServiceStack", role, codebaseBucket, commonLambdaLayer);
    }

    @Override
    public void construct() {
        final Code mediaManagementLambdaCode = Code.fromBucket(codebaseBucket, "likedancesport-media-management-lambda.jar");

        final IFunction mediaManagementFunction = buildSpringRestLambda(this, mediaManagementLambdaCode,
                "likedancesport-media-management-lambda",
                "com.likedancesport.MediaManagementLambdaHandler::handleRequest");


        final LambdaIntegration mediaManagementLambdaIntegration = LambdaIntegration.Builder.create(mediaManagementFunction)
                .requestTemplates(new HashMap<>() {{
                    put("application/json", "{ \"statusCode\": \"200\" }");
                }})
                .timeout(Duration.seconds(29))
                .proxy(true)
                .build();

        final MethodOptions defaultMethodOptions = MethodOptions.builder()
                .authorizationType(AuthorizationType.NONE)
                .apiKeyRequired(false)
                .build();

        RestApi mediaManagementApi = RestApi.Builder.create(this, "likedancesport-management-api")
                .restApiName("likedancesport-management-api")
                .deploy(true)
                .deployOptions(StageOptions.builder()
                        .stageName("dev")
                        .build())
                .defaultMethodOptions(MethodOptions.builder()
                        .authorizationType(AuthorizationType.NONE)
                        .build())
                .build();

        final Resource apiResource = mediaManagementApi.getRoot().addResource("api");

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
}
