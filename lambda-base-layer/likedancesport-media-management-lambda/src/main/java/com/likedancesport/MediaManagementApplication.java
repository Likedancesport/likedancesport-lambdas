package com.likedancesport;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MediaManagementApplication {
    public static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> LAMBDA_CONTAINER_HANDLER;
    public static void main(String[] args) throws ContainerInitializationException {
        System.out.println("------ STARTUP --------");
        LAMBDA_CONTAINER_HANDLER = SpringBootLambdaContainerHandler.getAwsProxyHandler(MediaManagementApplication.class);
    }
}