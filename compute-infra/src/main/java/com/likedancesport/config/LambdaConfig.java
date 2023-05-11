package com.likedancesport.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.IBucket;

import java.util.List;

@Configuration
public class LambdaConfig extends AbstractCdkConfig{

    public LambdaConfig(Stack stack, StackProps stackProps) {
        super(stack, stackProps);
    }

    @Bean
    public LayerVersion layerVersion(@Qualifier("codebaseBucket") IBucket codebaseBucket) {
        final Code commonLambdaLayerCode = Code.fromBucket(codebaseBucket, "likedancesport-layer-dependencies.jar");
        return LayerVersion.Builder.create(stack, "likedancesport-common-lambda-layer")
                .layerVersionName("likedancesport-common-lambda-layer")
                .compatibleArchitectures(List.of(Architecture.X86_64, Architecture.ARM_64))
                .code(commonLambdaLayerCode)
                .compatibleRuntimes(List.of(Runtime.JAVA_11))
                .description("Base layer with common dependencies")
                .build();
    }
}
