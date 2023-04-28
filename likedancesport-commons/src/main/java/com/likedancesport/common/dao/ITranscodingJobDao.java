package com.likedancesport.common.dao;

import com.likedancesport.common.model.internal.TranscodingJob;

public interface ITranscodingJobDao {
    TranscodingJob findById(String jobId);
    void deleteById(String jobId);

    TranscodingJob save(TranscodingJob transcodingJob);
}
