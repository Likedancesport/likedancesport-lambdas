package com.likedancesport.integration.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.sqs.Queue;

@Component
public class VideoUploadHandlerTriggerMapper extends SQSLambdaTriggerMapper {
    @Autowired
    public VideoUploadHandlerTriggerMapper(@Qualifier("videoUploadHandlerAlias") IFunction function,
                                           @Qualifier("videoUploadLambdaQueue") Queue queue) {
        super(function, queue);
    }
}
