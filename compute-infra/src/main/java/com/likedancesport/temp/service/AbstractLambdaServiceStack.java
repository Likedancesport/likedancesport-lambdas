package com.likedancesport.temp.service;

import com.likedancesport.temp.stacks.bucket_sub.eventbridge_sub.ComputeStack;
import com.likedancesport.temp.util.CdkUtils;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.NestedStack;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.lambda.Alias;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Version;
import software.amazon.awscdk.services.s3.IBucket;

import javax.annotation.PostConstruct;
import java.util.List;

public abstract class AbstractLambdaServiceStack extends NestedStack {
    protected final IRole role;
    protected final IBucket codebaseBucket;
    protected final LayerVersion commonLambdaLayer;

    public AbstractLambdaServiceStack(@NotNull ComputeStack scope, @NotNull String id, IRole role, IBucket codebaseBucket, LayerVersion commonLambdaLayer) {
        super(scope, id);
        this.role = role;
        this.codebaseBucket = codebaseBucket;
        this.commonLambdaLayer = commonLambdaLayer;
    }

    @PostConstruct
    public abstract void construct();

    protected IFunction buildSpringRestLambda(Stack stack, Code code, String functionName, String handler) {
        final Function restLambda = Function.Builder.create(stack, functionName)
                .functionName(functionName)
                .architecture(Architecture.X86_64)
                .runtime(Runtime.JAVA_11)
                .memorySize(2900)
                .role(role)
                .layers(List.of(commonLambdaLayer))
                .handler(handler)
                .code(code)
                .build();

        return getAlias(stack, functionName, restLambda);
    }

    protected IFunction buildSpringCloudFunctionLambda(Stack stack, Code code, String functionName) {
        final Function springCloudFunction = Function.Builder.create(stack, functionName)
                .architecture(Architecture.X86_64)
                .runtime(Runtime.JAVA_11)
                .memorySize(2900)
                .role(role)
                .layers(List.of(commonLambdaLayer))
                .functionName(functionName)
                .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
                .code(code)
                .build();

        return getAlias(stack, functionName, springCloudFunction);
    }

    @NotNull
    private static Alias getAlias(Stack stack, String functionName, Function function) {
        CdkUtils.setSnapStart(function);

        final Version version = function.getCurrentVersion();

        String aliasName = functionName + "-alias";

        return Alias.Builder.create(stack, aliasName)
                .aliasName(aliasName)
                .version(version)
                .build();
    }
}
