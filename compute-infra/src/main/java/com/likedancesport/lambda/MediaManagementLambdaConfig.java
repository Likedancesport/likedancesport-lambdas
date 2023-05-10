package com.likedancesport.lambda;

import com.likedancesport.annotation.SnapStart;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MediaManagementLambdaConfig extends AbstractLambdaConfig {
    private final IRole mediaManagementLambdaRole;

    @Autowired
    public MediaManagementLambdaConfig(Stack stack, StackProps stackProps,
                                       @Qualifier("mediaManagementRole") IRole mediaManagementLambdaRole,
                                       @Qualifier("codebaseBucket") IBucket codebaseBucket,
                                       @Qualifier("commonLayer") LayerVersion commonLayer) {
        super(stack, stackProps, codebaseBucket, commonLayer);
        this.mediaManagementLambdaRole = mediaManagementLambdaRole;
    }

    @Override
    @Bean(name = "mediaManagementCode")
    public Code code() {
        return Code.fromBucket(codebaseBucket, "likedancesport-media-management-lambda-1.0.jar");
    }

    @Override
    @Bean(name = "mediaManagementLambda")
    @SnapStart
    public Function function() {
        return Function.Builder.create(stack, "media-management-lambda")
                .architecture(Architecture.X86_64)
                .runtime(Runtime.JAVA_11)
                .memorySize(3000)
                .role(mediaManagementLambdaRole)
                .layers(List.of(commonLayer))
                .functionName("media-management-lambda")
                .handler("com.likedancesport.MediaManagementLambdaHandler::handleRequest")
                .code(code())
                .build();
    }

    @Override
    @Bean(name = "mediaManagementLambdaVersion")
    public Version version() {
        return function().getCurrentVersion();
    }

    @Override
    @Bean(name = "mediaManagementAlias")
    public Alias alias() {
        return Alias.Builder.create(stack, "media-management-alias")
                .aliasName("media-management-alias")
                .version(version())
                .build();
    }
}
