package com.likedancesport;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.likedancesport.common.utils.json.JsonUtils;
import com.likedancesport.model.aws.MediaConvertJobStateChangeEvent;
import com.likedancesport.service.ITranscodingJobCompleteHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class LearningVideoTranscodingJobCompleteHandlerFunction implements Function<SQSEvent, Void> {
    private final ITranscodingJobCompleteHandlerService transcodingJobCompleteHandlerService;

    @Autowired
    public LearningVideoTranscodingJobCompleteHandlerFunction(ITranscodingJobCompleteHandlerService transcodingJobCompleteHandlerService) {
        this.transcodingJobCompleteHandlerService = transcodingJobCompleteHandlerService;
    }

    @Override
    public Void apply(SQSEvent sqsEvent) {
        for (SQSEvent.SQSMessage eventRecord : sqsEvent.getRecords()) {
            MediaConvertJobStateChangeEvent mediaConvertJobStateChangeEvent =
                    JsonUtils.fromJson(eventRecord.getBody(), MediaConvertJobStateChangeEvent.class);
            transcodingJobCompleteHandlerService.handleJobComplete(mediaConvertJobStateChangeEvent);
        }
        return null;
    }
}
