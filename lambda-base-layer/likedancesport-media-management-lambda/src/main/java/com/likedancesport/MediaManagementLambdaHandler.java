package com.likedancesport;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class MediaManagementLambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    public MediaManagementLambdaHandler() throws ContainerInitializationException {
        MediaManagementApplication.main(new String[]{});
    }

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest awsProxyRequest, Context context) {
        return MediaManagementApplication.LAMBDA_CONTAINER_HANDLER.proxy(awsProxyRequest, context);
    }
}
