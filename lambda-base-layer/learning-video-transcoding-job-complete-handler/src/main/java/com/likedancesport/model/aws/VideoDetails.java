package com.likedancesport.model.aws;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "widthInPx",
        "heightInPx",
        "averageBitrate",
        "qvbrAvgQuality",
        "qvbrMinQuality",
        "qvbrMaxQuality",
        "qvbrMinQualityLocation",
        "qvbrMaxQualityLocation"
})
@Generated("jsonschema2pojo")
public class VideoDetails {

    @JsonProperty("widthInPx")
    private Integer widthInPx;
    @JsonProperty("heightInPx")
    private Integer heightInPx;
    @JsonProperty("averageBitrate")
    private Integer averageBitrate;
    @JsonProperty("qvbrAvgQuality")
    private Double qvbrAvgQuality;
    @JsonProperty("qvbrMinQuality")
    private Double qvbrMinQuality;
    @JsonProperty("qvbrMaxQuality")
    private Double qvbrMaxQuality;
    @JsonProperty("qvbrMinQualityLocation")
    private Integer qvbrMinQualityLocation;
    @JsonProperty("qvbrMaxQualityLocation")
    private Integer qvbrMaxQualityLocation;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("widthInPx")
    public Integer getWidthInPx() {
        return widthInPx;
    }

    @JsonProperty("widthInPx")
    public void setWidthInPx(Integer widthInPx) {
        this.widthInPx = widthInPx;
    }

    public VideoDetails withWidthInPx(Integer widthInPx) {
        this.widthInPx = widthInPx;
        return this;
    }

    @JsonProperty("heightInPx")
    public Integer getHeightInPx() {
        return heightInPx;
    }

    @JsonProperty("heightInPx")
    public void setHeightInPx(Integer heightInPx) {
        this.heightInPx = heightInPx;
    }

    public VideoDetails withHeightInPx(Integer heightInPx) {
        this.heightInPx = heightInPx;
        return this;
    }

    @JsonProperty("averageBitrate")
    public Integer getAverageBitrate() {
        return averageBitrate;
    }

    @JsonProperty("averageBitrate")
    public void setAverageBitrate(Integer averageBitrate) {
        this.averageBitrate = averageBitrate;
    }

    public VideoDetails withAverageBitrate(Integer averageBitrate) {
        this.averageBitrate = averageBitrate;
        return this;
    }

    @JsonProperty("qvbrAvgQuality")
    public Double getQvbrAvgQuality() {
        return qvbrAvgQuality;
    }

    @JsonProperty("qvbrAvgQuality")
    public void setQvbrAvgQuality(Double qvbrAvgQuality) {
        this.qvbrAvgQuality = qvbrAvgQuality;
    }

    public VideoDetails withQvbrAvgQuality(Double qvbrAvgQuality) {
        this.qvbrAvgQuality = qvbrAvgQuality;
        return this;
    }

    @JsonProperty("qvbrMinQuality")
    public Double getQvbrMinQuality() {
        return qvbrMinQuality;
    }

    @JsonProperty("qvbrMinQuality")
    public void setQvbrMinQuality(Double qvbrMinQuality) {
        this.qvbrMinQuality = qvbrMinQuality;
    }

    public VideoDetails withQvbrMinQuality(Double qvbrMinQuality) {
        this.qvbrMinQuality = qvbrMinQuality;
        return this;
    }

    @JsonProperty("qvbrMaxQuality")
    public Double getQvbrMaxQuality() {
        return qvbrMaxQuality;
    }

    @JsonProperty("qvbrMaxQuality")
    public void setQvbrMaxQuality(Double qvbrMaxQuality) {
        this.qvbrMaxQuality = qvbrMaxQuality;
    }

    public VideoDetails withQvbrMaxQuality(Double qvbrMaxQuality) {
        this.qvbrMaxQuality = qvbrMaxQuality;
        return this;
    }

    @JsonProperty("qvbrMinQualityLocation")
    public Integer getQvbrMinQualityLocation() {
        return qvbrMinQualityLocation;
    }

    @JsonProperty("qvbrMinQualityLocation")
    public void setQvbrMinQualityLocation(Integer qvbrMinQualityLocation) {
        this.qvbrMinQualityLocation = qvbrMinQualityLocation;
    }

    public VideoDetails withQvbrMinQualityLocation(Integer qvbrMinQualityLocation) {
        this.qvbrMinQualityLocation = qvbrMinQualityLocation;
        return this;
    }

    @JsonProperty("qvbrMaxQualityLocation")
    public Integer getQvbrMaxQualityLocation() {
        return qvbrMaxQualityLocation;
    }

    @JsonProperty("qvbrMaxQualityLocation")
    public void setQvbrMaxQualityLocation(Integer qvbrMaxQualityLocation) {
        this.qvbrMaxQualityLocation = qvbrMaxQualityLocation;
    }

    public VideoDetails withQvbrMaxQualityLocation(Integer qvbrMaxQualityLocation) {
        this.qvbrMaxQualityLocation = qvbrMaxQualityLocation;
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

    public VideoDetails withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(VideoDetails.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("widthInPx");
        sb.append('=');
        sb.append(((this.widthInPx == null) ? "<null>" : this.widthInPx));
        sb.append(',');
        sb.append("heightInPx");
        sb.append('=');
        sb.append(((this.heightInPx == null) ? "<null>" : this.heightInPx));
        sb.append(',');
        sb.append("averageBitrate");
        sb.append('=');
        sb.append(((this.averageBitrate == null) ? "<null>" : this.averageBitrate));
        sb.append(',');
        sb.append("qvbrAvgQuality");
        sb.append('=');
        sb.append(((this.qvbrAvgQuality == null) ? "<null>" : this.qvbrAvgQuality));
        sb.append(',');
        sb.append("qvbrMinQuality");
        sb.append('=');
        sb.append(((this.qvbrMinQuality == null) ? "<null>" : this.qvbrMinQuality));
        sb.append(',');
        sb.append("qvbrMaxQuality");
        sb.append('=');
        sb.append(((this.qvbrMaxQuality == null) ? "<null>" : this.qvbrMaxQuality));
        sb.append(',');
        sb.append("qvbrMinQualityLocation");
        sb.append('=');
        sb.append(((this.qvbrMinQualityLocation == null) ? "<null>" : this.qvbrMinQualityLocation));
        sb.append(',');
        sb.append("qvbrMaxQualityLocation");
        sb.append('=');
        sb.append(((this.qvbrMaxQualityLocation == null) ? "<null>" : this.qvbrMaxQualityLocation));
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
        result = ((result * 31) + ((this.heightInPx == null) ? 0 : this.heightInPx.hashCode()));
        result = ((result * 31) + ((this.qvbrAvgQuality == null) ? 0 : this.qvbrAvgQuality.hashCode()));
        result = ((result * 31) + ((this.qvbrMinQuality == null) ? 0 : this.qvbrMinQuality.hashCode()));
        result = ((result * 31) + ((this.qvbrMaxQualityLocation == null) ? 0 : this.qvbrMaxQualityLocation.hashCode()));
        result = ((result * 31) + ((this.qvbrMinQualityLocation == null) ? 0 : this.qvbrMinQualityLocation.hashCode()));
        result = ((result * 31) + ((this.widthInPx == null) ? 0 : this.widthInPx.hashCode()));
        result = ((result * 31) + ((this.additionalProperties == null) ? 0 : this.additionalProperties.hashCode()));
        result = ((result * 31) + ((this.averageBitrate == null) ? 0 : this.averageBitrate.hashCode()));
        result = ((result * 31) + ((this.qvbrMaxQuality == null) ? 0 : this.qvbrMaxQuality.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VideoDetails) == false) {
            return false;
        }
        VideoDetails rhs = ((VideoDetails) other);
        return ((((((((((this.heightInPx == rhs.heightInPx) || ((this.heightInPx != null) && this.heightInPx.equals(rhs.heightInPx))) && ((this.qvbrAvgQuality == rhs.qvbrAvgQuality) || ((this.qvbrAvgQuality != null) && this.qvbrAvgQuality.equals(rhs.qvbrAvgQuality)))) && ((this.qvbrMinQuality == rhs.qvbrMinQuality) || ((this.qvbrMinQuality != null) && this.qvbrMinQuality.equals(rhs.qvbrMinQuality)))) && ((this.qvbrMaxQualityLocation == rhs.qvbrMaxQualityLocation) || ((this.qvbrMaxQualityLocation != null) && this.qvbrMaxQualityLocation.equals(rhs.qvbrMaxQualityLocation)))) && ((this.qvbrMinQualityLocation == rhs.qvbrMinQualityLocation) || ((this.qvbrMinQualityLocation != null) && this.qvbrMinQualityLocation.equals(rhs.qvbrMinQualityLocation)))) && ((this.widthInPx == rhs.widthInPx) || ((this.widthInPx != null) && this.widthInPx.equals(rhs.widthInPx)))) && ((this.additionalProperties == rhs.additionalProperties) || ((this.additionalProperties != null) && this.additionalProperties.equals(rhs.additionalProperties)))) && ((this.averageBitrate == rhs.averageBitrate) || ((this.averageBitrate != null) && this.averageBitrate.equals(rhs.averageBitrate)))) && ((this.qvbrMaxQuality == rhs.qvbrMaxQuality) || ((this.qvbrMaxQuality != null) && this.qvbrMaxQuality.equals(rhs.qvbrMaxQuality))));
    }

}
