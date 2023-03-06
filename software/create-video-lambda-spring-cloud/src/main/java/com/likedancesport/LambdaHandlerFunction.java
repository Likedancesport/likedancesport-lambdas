package com.likedancesport;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.likedancesport.model.CreateVideoRequest;
import com.likedancesport.model.CreateVideoResult;
import com.likedancesport.service.IVideoService;
import com.likedancesport.common.utils.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
public class LambdaHandlerFunction implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final IVideoService videoService;

    @Autowired
    public LambdaHandlerFunction(IVideoService videoService) {
        this.videoService = videoService;
    }

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent) {
        System.out.println("-----Handling request");
        CreateVideoRequest createVideoRequest = JsonUtils.fromJson(apiGatewayProxyRequestEvent.getBody(), CreateVideoRequest.class);
        CreateVideoResult createVideoResult = videoService.createVideo(createVideoRequest);
        return new APIGatewayProxyResponseEvent()
                .withBody(JsonUtils.toJson(createVideoResult))
                .withHeaders(Map.of("presigned-url", createVideoResult.getPresignedUrl().toString()))
                .withStatusCode(201);
    }
}
