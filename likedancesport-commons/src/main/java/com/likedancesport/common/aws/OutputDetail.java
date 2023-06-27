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
        "outputFilePaths",
        "durationInMs",
        "videoDetails"
})
@Generated("jsonschema2pojo")
public class OutputDetail {

    @JsonProperty("outputFilePaths")
    private List<String> outputFilePaths = new ArrayList<String>();
    @JsonProperty("durationInMs")
    private Integer durationInMs;
    @JsonProperty("videoDetails")
    private VideoDetails videoDetails;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("outputFilePaths")
    public List<String> getOutputFilePaths() {
        return outputFilePaths;
    }

    @JsonProperty("outputFilePaths")
    public void setOutputFilePaths(List<String> outputFilePaths) {
        this.outputFilePaths = outputFilePaths;
    }

    public OutputDetail withOutputFilePaths(List<String> outputFilePaths) {
        this.outputFilePaths = outputFilePaths;
        return this;
    }

    @JsonProperty("durationInMs")
    public Integer getDurationInMs() {
        return durationInMs;
    }

    @JsonProperty("durationInMs")
    public void setDurationInMs(Integer durationInMs) {
        this.durationInMs = durationInMs;
    }

    public OutputDetail withDurationInMs(Integer durationInMs) {
        this.durationInMs = durationInMs;
        return this;
    }

    @JsonProperty("videoDetails")
    public VideoDetails getVideoDetails() {
        return videoDetails;
    }

    @JsonProperty("videoDetails")
    public void setVideoDetails(VideoDetails videoDetails) {
        this.videoDetails = videoDetails;
    }

    public OutputDetail withVideoDetails(VideoDetails videoDetails) {
        this.videoDetails = videoDetails;
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

    public OutputDetail withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OutputDetail.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("outputFilePaths");
        sb.append('=');
        sb.append(((this.outputFilePaths == null) ? "<null>" : this.outputFilePaths));
        sb.append(',');
        sb.append("durationInMs");
        sb.append('=');
        sb.append(((this.durationInMs == null) ? "<null>" : this.durationInMs));
        sb.append(',');
        sb.append("videoDetails");
        sb.append('=');
        sb.append(((this.videoDetails == null) ? "<null>" : this.videoDetails));
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
        result = ((result * 31) + ((this.durationInMs == null) ? 0 : this.durationInMs.hashCode()));
        result = ((result * 31) + ((this.additionalProperties == null) ? 0 : this.additionalProperties.hashCode()));
        result = ((result * 31) + ((this.outputFilePaths == null) ? 0 : this.outputFilePaths.hashCode()));
        result = ((result * 31) + ((this.videoDetails == null) ? 0 : this.videoDetails.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OutputDetail) == false) {
            return false;
        }
        OutputDetail rhs = ((OutputDetail) other);
        return (((((this.durationInMs == rhs.durationInMs) || ((this.durationInMs != null) && this.durationInMs.equals(rhs.durationInMs))) && ((this.additionalProperties == rhs.additionalProperties) || ((this.additionalProperties != null) && this.additionalProperties.equals(rhs.additionalProperties)))) && ((this.outputFilePaths == rhs.outputFilePaths) || ((this.outputFilePaths != null) && this.outputFilePaths.equals(rhs.outputFilePaths)))) && ((this.videoDetails == rhs.videoDetails) || ((this.videoDetails != null) && this.videoDetails.equals(rhs.videoDetails))));
    }

}
