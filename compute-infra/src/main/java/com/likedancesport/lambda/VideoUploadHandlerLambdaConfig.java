package com.likedancesport.lambda;

import com.likedancesport.annotation.SnapStart;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.lambda.Alias;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Version;
import software.amazon.awscdk.services.s3.IBucket;

import java.util.List;

@Configuration
public class VideoUploadHandlerLambdaConfig extends AbstractLambdaConfig {
    private final IRole videoUploadHandlerRole;

    public VideoUploadHandlerLambdaConfig(Stack stack, StackProps stackProps,
                                          IBucket codebaseBucket,
                                          @Qualifier("commonLayer") LayerVersion commonLayer,
                                          @Qualifier("videoUploadHandlerRole") IRole videoUploadHandlerRole) {
        super(stack, stackProps, codebaseBucket, commonLayer);
        this.videoUploadHandlerRole = videoUploadHandlerRole;
    }

    @Override
    @Bean(name = "videoUploadHandlerCode")
    public Code code() {
        return Code.fromBucket(codebaseBucket, "mp4-video-upload-handler-1.0.jar");
    }

    @Override
    @Bean(name = "videoUploadHandlerLambda")
    @SnapStart
    public Function function() {
        return Function.Builder.create(stack, "mp4-video-upload-handler")
                .architecture(Architecture.X86_64)
                .runtime(Runtime.JAVA_11)
                .memorySize(3000)
                .role(videoUploadHandlerRole)
                .layers(List.of(commonLayer))
                .functionName("video-upload-handler")
                .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
                .code(code())
                .build();
    }

    @Override
    @Bean(name = "videoUploadHandlerAlias")
    public Alias alias() {
        return Alias.Builder.create(stack, "video-upload-handler-alias")
                .aliasName("snap-v-alias")
                .version(version())
                .build();
    }

    @Override
    @Bean(name = "videoUploadHandlerVersion")
    public Version version() {
        return function().getCurrentVersion();
    }
}
