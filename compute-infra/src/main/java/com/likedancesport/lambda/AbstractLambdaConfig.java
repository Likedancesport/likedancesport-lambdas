package com.likedancesport.lambda;

import com.likedancesport.config.AbstractCdkConfig;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Alias;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.Version;
import software.amazon.awscdk.services.s3.IBucket;

public abstract class AbstractLambdaConfig extends AbstractCdkConfig {
    protected final IBucket codebaseBucket;
    protected final LayerVersion commonLayer;

    @Autowired
    public AbstractLambdaConfig(Stack stack, StackProps stackProps, IBucket codebaseBucket, LayerVersion commonLayer) {
        super(stack, stackProps);
        this.codebaseBucket = codebaseBucket;
        this.commonLayer = commonLayer;
    }

    public abstract Code code();

    public abstract Function function();

    public abstract Alias alias();

    public abstract Version version();
}
