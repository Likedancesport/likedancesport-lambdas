package com.likedancesport.model.aws;

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
        "outputDetails",
        "playlistFilePaths",
        "type"
})
@Generated("jsonschema2pojo")
public class OutputGroupDetail {

    @JsonProperty("outputDetails")
    private List<OutputDetail> outputDetails = new ArrayList<OutputDetail>();
    @JsonProperty("playlistFilePaths")
    private List<String> playlistFilePaths = new ArrayList<String>();
    @JsonProperty("type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("outputDetails")
    public List<OutputDetail> getOutputDetails() {
        return outputDetails;
    }

    @JsonProperty("outputDetails")
    public void setOutputDetails(List<OutputDetail> outputDetails) {
        this.outputDetails = outputDetails;
    }

    public OutputGroupDetail withOutputDetails(List<OutputDetail> outputDetails) {
        this.outputDetails = outputDetails;
        return this;
    }

    @JsonProperty("playlistFilePaths")
    public List<String> getPlaylistFilePaths() {
        return playlistFilePaths;
    }

    @JsonProperty("playlistFilePaths")
    public void setPlaylistFilePaths(List<String> playlistFilePaths) {
        this.playlistFilePaths = playlistFilePaths;
    }

    public OutputGroupDetail withPlaylistFilePaths(List<String> playlistFilePaths) {
        this.playlistFilePaths = playlistFilePaths;
        return this;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public OutputGroupDetail withType(String type) {
        this.type = type;
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

    public OutputGroupDetail withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OutputGroupDetail.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("outputDetails");
        sb.append('=');
        sb.append(((this.outputDetails == null) ? "<null>" : this.outputDetails));
        sb.append(',');
        sb.append("playlistFilePaths");
        sb.append('=');
        sb.append(((this.playlistFilePaths == null) ? "<null>" : this.playlistFilePaths));
        sb.append(',');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null) ? "<null>" : this.type));
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
        result = ((result * 31) + ((this.outputDetails == null) ? 0 : this.outputDetails.hashCode()));
        result = ((result * 31) + ((this.playlistFilePaths == null) ? 0 : this.playlistFilePaths.hashCode()));
        result = ((result * 31) + ((this.additionalProperties == null) ? 0 : this.additionalProperties.hashCode()));
        result = ((result * 31) + ((this.type == null) ? 0 : this.type.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OutputGroupDetail) == false) {
            return false;
        }
        OutputGroupDetail rhs = ((OutputGroupDetail) other);
        return (((((this.outputDetails == rhs.outputDetails) || ((this.outputDetails != null) && this.outputDetails.equals(rhs.outputDetails))) && ((this.playlistFilePaths == rhs.playlistFilePaths) || ((this.playlistFilePaths != null) && this.playlistFilePaths.equals(rhs.playlistFilePaths)))) && ((this.additionalProperties == rhs.additionalProperties) || ((this.additionalProperties != null) && this.additionalProperties.equals(rhs.additionalProperties)))) && ((this.type == rhs.type) || ((this.type != null) && this.type.equals(rhs.type))));
    }

}
