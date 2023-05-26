package com.likedancesport;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.fasterxml.jackson.databind.JsonNode;
import com.likedancesport.common.model.domain.S3Key;
import com.likedancesport.common.utils.JsonUtils;
import com.likedancesport.service.IVideoProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
//        processS3Event(s3Event);
        for (SQSEvent.SQSMessage sqsMessage : sqsEvent.getRecords()) {
            JsonNode jsonNode = JsonUtils.parseStringToNode(sqsMessage.getBody());
            JsonNode recordsNode = jsonNode.get("Records");
            List<S3EventNotification.S3EventNotificationRecord> records = new ArrayList<>();
            recordsNode.spliterator().forEachRemaining(recordNode ->
                    records.add(JsonUtils.parseJsonNode(recordNode, S3EventNotification.S3EventNotificationRecord.class)));

            S3Event s3Event = new S3Event(records);
            log.debug("----- HANDLING MESSAGE");
            log.debug("----- MESSAGE BODY: {}", sqsMessage.getBody());

//            S3Event s3Event = JsonUtils.s3EventFromJson(sqsMessage.getBody());
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
