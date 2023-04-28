package com.likedancesport.common.dao.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.likedancesport.common.dao.ITranscodingJobDao;
import com.likedancesport.common.model.internal.TranscodingJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DynamoDbTranscodingJobDao implements ITranscodingJobDao {
    private final DynamoDBMapper dynamoDBMapper;

    @Autowired
    public DynamoDbTranscodingJobDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public TranscodingJob findById(String jobId) {
        TranscodingJob transcodingJob = dynamoDBMapper.load(TranscodingJob.class, jobId);
        if (transcodingJob == null) {
            throw new RuntimeException("No such job");
        }
        return transcodingJob;
    }

    @Override
    public void deleteById(String jobId) {
        try {
            TranscodingJob transcodingJob = findById(jobId);
            dynamoDBMapper.delete(transcodingJob);
        } catch (RuntimeException ignored) {
        }
    }

    @Override
    public TranscodingJob save(TranscodingJob transcodingJob) {
        dynamoDBMapper.save(transcodingJob);
        return transcodingJob;
    }
}
