package com.likedancesport.service;

import com.likedancesport.model.aws.MediaConvertJobStateChangeEvent;

public interface ITranscodingJobCompleteHandlerService {
    void handleJobComplete(MediaConvertJobStateChangeEvent mediaConvertJobStateChangeEvent);
}
