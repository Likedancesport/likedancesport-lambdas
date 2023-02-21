package com.likedancesport;

import software.amazon.awscdk.BundlingOptions;
import software.amazon.awscdk.BundlingOutput;
import software.amazon.awscdk.DockerVolume;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.lambda.Alias;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.CfnFunction;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Version;
import software.amazon.awscdk.services.s3.assets.AssetOptions;
import software.constructs.Construct;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;

public class ComputeServerlessStack extends Stack {
    public ComputeServerlessStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public ComputeServerlessStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        CfnFunction.SnapStartProperty snapStartProperty = CfnFunction.SnapStartProperty.builder()
                .applyOn("PublishedVersions")
                .build();

        final IRole role = Role.fromRoleArn(this, "lambda-role", "arn:aws:iam::066002146890:role/Rds-S3-SSM-Role");

        List<String> buildingCommands = Arrays.asList(
                "/bin/sh",
                "-c",
                "ls -la" +
                "&& mvn clean install " +
                        "&& cp /asset-input/target/create-video-lambda-spring-cloud-1.0-SNAPSHOT-aws.jar /asset-output/");

        BundlingOptions createVideoLambdaBundlingOptions = BundlingOptions.builder()
                .command(buildingCommands)
                .image(Runtime.JAVA_11.getBundlingImage())
                .user("root")
                .outputType(BundlingOutput.ARCHIVED)
                .volumes(singletonList(DockerVolume.builder()
                        .hostPath(System.getProperty("user.home") + "/.m2/") .containerPath("/root/.m2/")
                        .build()))
                .build();

        final Code createVideoLambdaCode = Code.fromAsset("../../software/create-video-lambda-spring-cloud", AssetOptions.builder()
                .bundling(createVideoLambdaBundlingOptions)
                .build());

        final Function createVideoLambda = Function.Builder.create(this, "create-video-lambda-cdk")
                .architecture(Architecture.X86_64)
                .runtime(Runtime.JAVA_11)
                .memorySize(1024)
                .role(role)
                .functionName("create-video-lambda-cdk")
                .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
                .code(createVideoLambdaCode)
                .build();

        ((CfnFunction) createVideoLambda.getNode().getDefaultChild()).setSnapStart(snapStartProperty);

        final Version version = createVideoLambda.getCurrentVersion();

        Alias alias = Alias.Builder.create(this, "func-alias")
                .aliasName("snap-alias")
                .version(version)
                .build();

    }
}
