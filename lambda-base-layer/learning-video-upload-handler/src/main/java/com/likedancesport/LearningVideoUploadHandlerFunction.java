package com.likedancesport;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.likedancesport.common.model.domain.S3Key;
import com.likedancesport.common.utils.JsonUtils;
import com.likedancesport.service.IVideoProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Component
@Slf4j
public class LearningVideoUploadHandlerFunction implements Function<SQSEvent, Void> {
    private final IVideoProcessingService videoEncodingService;

    @Autowired
    public LearningVideoUploadHandlerFunction(IVideoProcessingService videoEncodingService) {
        log.info("---- FUNCTION INIT");
        this.videoEncodingService = videoEncodingService;
    }

    @Override
    @Transactional
    public Void apply(SQSEvent sqsEvent) {
        // TODO: try to implement with SQS
        for (SQSEvent.SQSMessage sqsMessage : sqsEvent.getRecords()) {
            log.debug("----- HANDLING MESSAGE");
            log.debug("----- MESSAGE BODY: {}", sqsMessage.getBody());
            List<S3Key> s3Keys = JsonUtils.getS3KeysFromS3Event(sqsMessage.getBody());
            s3Keys.forEach(videoEncodingService::processVideo);
        }
        return null;
    }

}
