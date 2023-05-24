package com.likedancesport;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.likedancesport.common.aws.MediaConvertJobStateChangeEvent;
import com.likedancesport.common.utils.JsonUtils;
import com.likedancesport.service.ITranscodingJobCompleteHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@Slf4j
public class LearningVideoTranscodingJobCompleteHandlerFunction implements Function<SQSEvent, Void> {
    private final ITranscodingJobCompleteHandlerService transcodingJobCompleteHandlerService;

    @Autowired
    public LearningVideoTranscodingJobCompleteHandlerFunction(ITranscodingJobCompleteHandlerService transcodingJobCompleteHandlerService) {
        this.transcodingJobCompleteHandlerService = transcodingJobCompleteHandlerService;
    }

    @Override
    public Void apply(SQSEvent sqsEvent) {
        for (SQSEvent.SQSMessage eventRecord : sqsEvent.getRecords()) {
            log.info("----- Handling message");
            log.info("----- Message body: {}", eventRecord.getBody());
            try {
                MediaConvertJobStateChangeEvent mediaConvertJobStateChangeEvent =
                        JsonUtils.fromJson(eventRecord.getBody(), MediaConvertJobStateChangeEvent.class);
                transcodingJobCompleteHandlerService.handleJobComplete(mediaConvertJobStateChangeEvent);
            } catch (RuntimeException e) {
                log.error("AN ERROR OCCURED");
                log.error(e.getMessage());
            }
        }
        return null;
    }
}
