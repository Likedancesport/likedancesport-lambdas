package com.likedancesport.service;

import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.s3.IBucket;

public abstract class AbstractLambdaServiceConstruct implements IServiceConstruct {
    protected final IRole role;
    protected final IBucket codebaseBucket;
    protected final LayerVersion commonLambdaLayer;

    public AbstractLambdaServiceConstruct(IRole role, IBucket codebaseBucket, LayerVersion commonLambdaLayer) {
        this.role = role;
        this.codebaseBucket = codebaseBucket;
        this.commonLambdaLayer = commonLambdaLayer;
    }
}
