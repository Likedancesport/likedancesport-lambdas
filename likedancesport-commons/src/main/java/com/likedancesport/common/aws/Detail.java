package com.likedancesport.common.aws;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "timestamp",
        "accountId",
        "queue",
        "jobId",
        "status",
        "userMetadata",
        "outputGroupDetails",
        "paddingInserted",
        "blackVideoDetected",
        "blackVideoSegments"
})
@Generated("jsonschema2pojo")
public class Detail {

    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("accountId")
    private String accountId;
    @JsonProperty("queue")
    private String queue;
    @JsonProperty("jobId")
    private String jobId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("userMetadata")
    private UserMetadata userMetadata;
    @JsonProperty("outputGroupDetails")
    private List<OutputGroupDetail> outputGroupDetails = new ArrayList<OutputGroupDetail>();
    @JsonProperty("paddingInserted")
    private Integer paddingInserted;
    @JsonProperty("blackVideoDetected")
    private Integer blackVideoDetected;
    @JsonProperty("blackVideoSegments")
    private List<BlackVideoSegment> blackVideoSegments = new ArrayList<BlackVideoSegment>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Detail withTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @JsonProperty("accountId")
    public String getAccountId() {
        return accountId;
    }

    @JsonProperty("accountId")
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Detail withAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    @JsonProperty("queue")
    public String getQueue() {
        return queue;
    }

    @JsonProperty("queue")
    public void setQueue(String queue) {
        this.queue = queue;
    }

    public Detail withQueue(String queue) {
        this.queue = queue;
        return this;
    }

    @JsonProperty("jobId")
    public String getJobId() {
        return jobId;
    }

    @JsonProperty("jobId")
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Detail withJobId(String jobId) {
        this.jobId = jobId;
        return this;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    public Detail withStatus(String status) {
        this.status = status;
        return this;
    }

    @JsonProperty("userMetadata")
    public UserMetadata getUserMetadata() {
        return userMetadata;
    }

    @JsonProperty("userMetadata")
    public void setUserMetadata(UserMetadata userMetadata) {
        this.userMetadata = userMetadata;
    }

    public Detail withUserMetadata(UserMetadata userMetadata) {
        this.userMetadata = userMetadata;
        return this;
    }

    @JsonProperty("outputGroupDetails")
    public List<OutputGroupDetail> getOutputGroupDetails() {
        return outputGroupDetails;
    }

    @JsonProperty("outputGroupDetails")
    public void setOutputGroupDetails(List<OutputGroupDetail> outputGroupDetails) {
        this.outputGroupDetails = outputGroupDetails;
    }

    public Detail withOutputGroupDetails(List<OutputGroupDetail> outputGroupDetails) {
        this.outputGroupDetails = outputGroupDetails;
        return this;
    }

    @JsonProperty("paddingInserted")
    public Integer getPaddingInserted() {
        return paddingInserted;
    }

    @JsonProperty("paddingInserted")
    public void setPaddingInserted(Integer paddingInserted) {
        this.paddingInserted = paddingInserted;
    }

    public Detail withPaddingInserted(Integer paddingInserted) {
        this.paddingInserted = paddingInserted;
        return this;
    }

    @JsonProperty("blackVideoDetected")
    public Integer getBlackVideoDetected() {
        return blackVideoDetected;
    }

    @JsonProperty("blackVideoDetected")
    public void setBlackVideoDetected(Integer blackVideoDetected) {
        this.blackVideoDetected = blackVideoDetected;
    }

    public Detail withBlackVideoDetected(Integer blackVideoDetected) {
        this.blackVideoDetected = blackVideoDetected;
        return this;
    }

    @JsonProperty("blackVideoSegments")
    public List<BlackVideoSegment> getBlackVideoSegments() {
        return blackVideoSegments;
    }

    @JsonProperty("blackVideoSegments")
    public void setBlackVideoSegments(List<BlackVideoSegment> blackVideoSegments) {
        this.blackVideoSegments = blackVideoSegments;
    }

    public Detail withBlackVideoSegments(List<BlackVideoSegment> blackVideoSegments) {
        this.blackVideoSegments = blackVideoSegments;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Detail withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Detail.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("timestamp");
        sb.append('=');
        sb.append(((this.timestamp == null) ? "<null>" : this.timestamp));
        sb.append(',');
        sb.append("accountId");
        sb.append('=');
        sb.append(((this.accountId == null) ? "<null>" : this.accountId));
        sb.append(',');
        sb.append("queue");
        sb.append('=');
        sb.append(((this.queue == null) ? "<null>" : this.queue));
        sb.append(',');
        sb.append("jobId");
        sb.append('=');
        sb.append(((this.jobId == null) ? "<null>" : this.jobId));
        sb.append(',');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null) ? "<null>" : this.status));
        sb.append(',');
        sb.append("userMetadata");
        sb.append('=');
        sb.append(((this.userMetadata == null) ? "<null>" : this.userMetadata));
        sb.append(',');
        sb.append("outputGroupDetails");
        sb.append('=');
        sb.append(((this.outputGroupDetails == null) ? "<null>" : this.outputGroupDetails));
        sb.append(',');
        sb.append("paddingInserted");
        sb.append('=');
        sb.append(((this.paddingInserted == null) ? "<null>" : this.paddingInserted));
        sb.append(',');
        sb.append("blackVideoDetected");
        sb.append('=');
        sb.append(((this.blackVideoDetected == null) ? "<null>" : this.blackVideoDetected));
        sb.append(',');
        sb.append("blackVideoSegments");
        sb.append('=');
        sb.append(((this.blackVideoSegments == null) ? "<null>" : this.blackVideoSegments));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null) ? "<null>" : this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.accountId == null) ? 0 : this.accountId.hashCode()));
        result = ((result * 31) + ((this.jobId == null) ? 0 : this.jobId.hashCode()));
        result = ((result * 31) + ((this.outputGroupDetails == null) ? 0 : this.outputGroupDetails.hashCode()));
        result = ((result * 31) + ((this.blackVideoSegments == null) ? 0 : this.blackVideoSegments.hashCode()));
        result = ((result * 31) + ((this.blackVideoDetected == null) ? 0 : this.blackVideoDetected.hashCode()));
        result = ((result * 31) + ((this.additionalProperties == null) ? 0 : this.additionalProperties.hashCode()));
        result = ((result * 31) + ((this.paddingInserted == null) ? 0 : this.paddingInserted.hashCode()));
        result = ((result * 31) + ((this.userMetadata == null) ? 0 : this.userMetadata.hashCode()));
        result = ((result * 31) + ((this.queue == null) ? 0 : this.queue.hashCode()));
        result = ((result * 31) + ((this.timestamp == null) ? 0 : this.timestamp.hashCode()));
        result = ((result * 31) + ((this.status == null) ? 0 : this.status.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Detail) == false) {
            return false;
        }
        Detail rhs = ((Detail) other);
        return ((((((((((((this.accountId == rhs.accountId) || ((this.accountId != null) && this.accountId.equals(rhs.accountId))) && ((this.jobId == rhs.jobId) || ((this.jobId != null) && this.jobId.equals(rhs.jobId)))) && ((this.outputGroupDetails == rhs.outputGroupDetails) || ((this.outputGroupDetails != null) && this.outputGroupDetails.equals(rhs.outputGroupDetails)))) && ((this.blackVideoSegments == rhs.blackVideoSegments) || ((this.blackVideoSegments != null) && this.blackVideoSegments.equals(rhs.blackVideoSegments)))) && ((this.blackVideoDetected == rhs.blackVideoDetected) || ((this.blackVideoDetected != null) && this.blackVideoDetected.equals(rhs.blackVideoDetected)))) && ((this.additionalProperties == rhs.additionalProperties) || ((this.additionalProperties != null) && this.additionalProperties.equals(rhs.additionalProperties)))) && ((this.paddingInserted == rhs.paddingInserted) || ((this.paddingInserted != null) && this.paddingInserted.equals(rhs.paddingInserted)))) && ((this.userMetadata == rhs.userMetadata) || ((this.userMetadata != null) && this.userMetadata.equals(rhs.userMetadata)))) && ((this.queue == rhs.queue) || ((this.queue != null) && this.queue.equals(rhs.queue)))) && ((this.timestamp == rhs.timestamp) || ((this.timestamp != null) && this.timestamp.equals(rhs.timestamp)))) && ((this.status == rhs.status) || ((this.status != null) && this.status.equals(rhs.status))));
    }

}
