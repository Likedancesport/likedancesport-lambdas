package com.likedancesport.lambda;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Alias;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.Version;
import software.amazon.awscdk.services.s3.IBucket;

public class LearningVideoTranscodingJobCompleteLambdaConfig extends AbstractLambdaConfig{
    public LearningVideoTranscodingJobCompleteLambdaConfig(Stack stack, StackProps stackProps, IBucket codebaseBucket, LayerVersion commonLayer) {
        super(stack, stackProps, codebaseBucket, commonLayer);
    }

    @Override
    public Code code() {
        return null;
    }

    @Override
    public Function function() {
        return null;
    }

    @Override
    public Alias alias() {
        return null;
    }

    @Override
    public Version version() {
        return null;
    }
}
