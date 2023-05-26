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
//TODO: try to implement with SQS
        for (SQSEvent.SQSMessage sqsMessage : sqsEvent.getRecords()) {
            log.debug("----- HANDLING MESSAGE");
            log.debug("----- MESSAGE BODY: {}", sqsMessage.getBody());
            S3Event s3Event = JsonUtils.s3EventFromJson(sqsMessage.getBody());
            log.debug("----- EVENT PARSED");
            processS3Event(s3Event);
        }
        return null;
    }

    private void processS3Event(S3Event s3Event) {
        log.debug("PROCESSING S3 EVENT");
        s3Event.getRecords().forEach(record ->
                videoEncodingService.processVideo(S3Key.of(record.getS3().getBucket().getName(),
                        record.getS3().getObject().getKey())));
    }
}
