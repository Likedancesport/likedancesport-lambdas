package com.likedancesport;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;

public class LikedancesportMarketplaceManager {
    public static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> LAMBDA_CONTAINER_HANDLER;

    public static void main(String[] args) throws ContainerInitializationException {
        System.out.println("------ STARTUP --------");
        LAMBDA_CONTAINER_HANDLER = SpringBootLambdaContainerHandler.getAwsProxyHandler(LikedancesportMarketplaceManager.class);
    }
}