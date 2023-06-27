package com.likedancesport.common.model.internal;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Builder;
import lombok.Data;

@DynamoDBTable(tableName = "TranscodingJobs")
@Builder
@Data
public class TranscodingJob {
    @DynamoDBHashKey(attributeName = "jobId")
    private String jobId;
    @DynamoDBAttribute(attributeName = "videoId")
    private Long videoId;
}
