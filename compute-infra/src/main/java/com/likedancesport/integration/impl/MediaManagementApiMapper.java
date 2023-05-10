package com.likedancesport.integration.impl;

import com.likedancesport.integration.ILambdaTriggerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.apigateway.AuthorizationType;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.Method;
import software.amazon.awscdk.services.apigateway.MethodOptions;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.lambda.Alias;
import software.amazon.awscdk.services.lambda.IFunction;

import java.util.HashMap;

import static com.likedancesport.util.DevOpsConstants.*;

@Component
public class MediaManagementApiMapper implements ILambdaTriggerMapper {
    private final RestApi mediaManagementApi;
    private final IFunction mediaManagementAlias;

    @Autowired
    public MediaManagementApiMapper(@Qualifier("mediaManagementApi") RestApi mediaManagementApi,
                                    @Qualifier("mediaManagementAlias") Alias mediaManagementAlias) {
        this.mediaManagementApi = mediaManagementApi;
        this.mediaManagementAlias = mediaManagementAlias;
    }

    @Override
    public void mapLambdas() {
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
