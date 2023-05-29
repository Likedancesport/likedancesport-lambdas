package com.likedancesport.temp.stacks.bucket_sub.eventbridge_sub;

import com.likedancesport.temp.stacks.bucket_sub.EventBridgeStack;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.NestedStack;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.IBucket;

import java.util.List;

@Configuration
public class ComputeStack extends NestedStack {

    public ComputeStack(@NotNull EventBridgeStack scope, @NotNull String id) {
        super(scope, "ComputeStack");
    }

    @Bean
    public LayerVersion layerVersion(@Qualifier("codebaseBucket") IBucket codebaseBucket) {
        final Code commonLambdaLayerCode = Code.fromBucket(codebaseBucket, "likedancesport-layer-dependencies.jar");
        return LayerVersion.Builder.create(this, "likedancesport-common-lambda-layer")
                .layerVersionName("likedancesport-common-lambda-layer")
                .compatibleArchitectures(List.of(Architecture.X86_64, Architecture.ARM_64))
                .code(commonLambdaLayerCode)
                .compatibleRuntimes(List.of(Runtime.JAVA_11))
                .description("Base layer with common dependencies")
                .build();
    }
}
