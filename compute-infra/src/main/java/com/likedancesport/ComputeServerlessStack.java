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
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Version;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.IBucket;
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
        IBucket deploymentBucket = Bucket.fromBucketArn(this, "testing-s3-mpu", "arn:aws:s3:::testing-s3-mpu");
        final Code commonLambdaLayerCode = Code.fromBucket(deploymentBucket, "deployment-lambdas/likedancesport-create-video-layer-dependencies.jar");
        /*buildCode("mvn clean install",
                "common-lambda-layer-1.0-aws.jar",
                "../software/common-lambda-layer");*/

        final LayerVersion commonLayer = LayerVersion.Builder.create(this, "common-lambda-layer")
                .layerVersionName("common-lambda-layer")
                .compatibleArchitectures(List.of(Architecture.X86_64, Architecture.ARM_64))
                .code(commonLambdaLayerCode)
                .compatibleRuntimes(List.of(Runtime.JAVA_11))
                .description("Base layer with common dependencies")
                .build();

        /*final Code springCloudFunctionLayerCode =
         *//* buildCode("mvn -PshadeProfile clean install",
                "spring-cloud-function-layer-1.0-shaded.jar",
                "../software/spring-cloud-function-layer");*//*

        final LayerVersion springCloudFunctionLayer = LayerVersion.Builder.create(this, "spring-cloud-function-layer")
                .layerVersionName("spring-cloud-function-layer")
                .compatibleArchitectures(List.of(Architecture.X86_64, Architecture.ARM_64))
                .code(springCloudFunctionLayerCode)
                .compatibleRuntimes(List.of(Runtime.JAVA_11))
                .description("layer with required dependencies")
                .build();*/

        final Code createVideoLambdaCode = Code.fromBucket(deploymentBucket, "deployment-lambdas/create-video-lambda-spring-cloud-1.0.jar");/* buildCode("mvn clean package",
                "create-video-lambda-spring-cloud-1.0-SNAPSHOT.jar",
                "../software/create-video-lambda-spring-cloud");*/

        final Function createVideoLambda = Function.Builder.create(this, "create-video-lambda-cdk-layered")
                .architecture(Architecture.X86_64)
                .runtime(Runtime.JAVA_11)
                .memorySize(1024)
                .role(role)
                .layers(List.of(commonLayer))
                .functionName("create-video-lambda-cdk-layered")
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

    private Code buildCode(String buildingCommand, String deploymentPackageFilename, String projectPath) {
        System.out.println("BUNDLING " + deploymentPackageFilename);
        List<String> buildingCommands = Arrays.asList(
                "/bin/sh",
                "-c",
                "ls -la && echo BUILDING" +
                        String.format("&& %s && echo COPY", buildingCommand) +
                        String.format("&& cp /asset-input/target/%s /asset-output/", deploymentPackageFilename));

        BundlingOptions bundlingOptions = BundlingOptions.builder()
                .command(buildingCommands)
                .image(Runtime.JAVA_11.getBundlingImage())
                .user("root")
                .outputType(BundlingOutput.ARCHIVED)
                .volumes(singletonList(DockerVolume.builder()
                        .hostPath(System.getProperty("user.home") + "/.m2/").containerPath("/root/.m2/")
                        .build()))
                .build();

        return Code.fromAsset(projectPath, AssetOptions.builder()
                .bundling(bundlingOptions)
                .build());
    }

}
