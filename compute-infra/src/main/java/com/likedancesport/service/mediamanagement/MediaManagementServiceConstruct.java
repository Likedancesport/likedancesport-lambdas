package com.likedancesport.service.mediamanagement;

import com.likedancesport.service.AbstractLambdaServiceConstruct;
import com.likedancesport.util.CdkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.AuthorizationType;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.Method;
import software.amazon.awscdk.services.apigateway.MethodOptions;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.lambda.Alias;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.HttpMethod;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Version;
import software.amazon.awscdk.services.s3.IBucket;

import java.util.HashMap;
import java.util.List;

@Component
public class MediaManagementServiceConstruct extends AbstractLambdaServiceConstruct {
    public static final String POST = HttpMethod.POST.name();
    public static final String DELETE = HttpMethod.DELETE.name();
    public static final String PUT = HttpMethod.PUT.name();
    public static final String GET = HttpMethod.GET.name();

    @Autowired
    public MediaManagementServiceConstruct(IRole role,
                                           @Qualifier("codebaseBucket") IBucket codebaseBucket,
                                           LayerVersion commonLambdaLayer) {
        super(role, codebaseBucket, commonLambdaLayer);
    }

    @Override
    public void construct(Stack stack, StackProps stackProps) {
        final Code mediaManagementLambdaCode = Code.fromBucket(codebaseBucket, "likedancesport-media-management-lambda-1.0.jar");

        final Function mediaManagementLambda = Function.Builder.create(stack, "media-management-lambda")
                .architecture(Architecture.X86_64)
                .runtime(Runtime.JAVA_11)
                .memorySize(3000)
                .role(role)
                .layers(List.of(commonLambdaLayer))
                .functionName("media-management-lambda")
                .handler("com.likedancesport.MediaManagementLambdaHandler::handleRequest")
                .code(mediaManagementLambdaCode)
                .build();

        CdkUtils.setSnapStart(mediaManagementLambda);

        final Version mediaManagementLambdaVersion = mediaManagementLambda.getCurrentVersion();

        final Alias mediaManagementAlias = Alias.Builder.create(stack, "media-management-alias")
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

        final MethodOptions defaultMethodOptions = MethodOptions.builder()
                .authorizationType(AuthorizationType.NONE)
                .apiKeyRequired(false)
                .build();

        RestApi mediaManagementApi = RestApi.Builder.create(stack, "likedancesport-management-api")
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
