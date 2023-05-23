package com.likedancesport;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MediaManagementApplication {
    public static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> LAMBDA_CONTAINER_HANDLER;

    public static void main(String[] args) throws ContainerInitializationException {
        log.info("------ STARTUP --------");
        LAMBDA_CONTAINER_HANDLER = SpringBootLambdaContainerHandler.getAwsProxyHandler(MediaManagementApplication.class);
    }
}