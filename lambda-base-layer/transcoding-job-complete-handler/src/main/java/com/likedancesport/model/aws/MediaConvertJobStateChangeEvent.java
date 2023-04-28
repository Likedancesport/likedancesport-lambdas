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
        "version",
        "id",
        "detail-type",
        "source",
        "account",
        "time",
        "region",
        "resources",
        "detail"
})
@Generated("jsonschema2pojo")
public class MediaConvertJobStateChangeEvent {

    @JsonProperty("version")
    private String version;
    @JsonProperty("id")
    private String id;
    @JsonProperty("detail-type")
    private String detailType;
    @JsonProperty("source")
    private String source;
    @JsonProperty("account")
    private String account;
    @JsonProperty("time")
    private String time;
    @JsonProperty("region")
    private String region;
    @JsonProperty("resources")
    private List<String> resources = new ArrayList<String>();
    @JsonProperty("detail")
    private Detail detail;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    public MediaConvertJobStateChangeEvent withVersion(String version) {
        this.version = version;
        return this;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public MediaConvertJobStateChangeEvent withId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("detail-type")
    public String getDetailType() {
        return detailType;
    }

    @JsonProperty("detail-type")
    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public MediaConvertJobStateChangeEvent withDetailType(String detailType) {
        this.detailType = detailType;
        return this;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    public MediaConvertJobStateChangeEvent withSource(String source) {
        this.source = source;
        return this;
    }

    @JsonProperty("account")
    public String getAccount() {
        return account;
    }

    @JsonProperty("account")
    public void setAccount(String account) {
        this.account = account;
    }

    public MediaConvertJobStateChangeEvent withAccount(String account) {
        this.account = account;
        return this;
    }

    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
    }

    public MediaConvertJobStateChangeEvent withTime(String time) {
        this.time = time;
        return this;
    }

    @JsonProperty("region")
    public String getRegion() {
        return region;
    }

    @JsonProperty("region")
    public void setRegion(String region) {
        this.region = region;
    }

    public MediaConvertJobStateChangeEvent withRegion(String region) {
        this.region = region;
        return this;
    }

    @JsonProperty("resources")
    public List<String> getResources() {
        return resources;
    }

    @JsonProperty("resources")
    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    public MediaConvertJobStateChangeEvent withResources(List<String> resources) {
        this.resources = resources;
        return this;
    }

    @JsonProperty("detail")
    public Detail getDetail() {
        return detail;
    }

    @JsonProperty("detail")
    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public MediaConvertJobStateChangeEvent withDetail(Detail detail) {
        this.detail = detail;
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

    public MediaConvertJobStateChangeEvent withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MediaConvertJobStateChangeEvent.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("version");
        sb.append('=');
        sb.append(((this.version == null) ? "<null>" : this.version));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null) ? "<null>" : this.id));
        sb.append(',');
        sb.append("detailType");
        sb.append('=');
        sb.append(((this.detailType == null) ? "<null>" : this.detailType));
        sb.append(',');
        sb.append("source");
        sb.append('=');
        sb.append(((this.source == null) ? "<null>" : this.source));
        sb.append(',');
        sb.append("account");
        sb.append('=');
        sb.append(((this.account == null) ? "<null>" : this.account));
        sb.append(',');
        sb.append("time");
        sb.append('=');
        sb.append(((this.time == null) ? "<null>" : this.time));
        sb.append(',');
        sb.append("region");
        sb.append('=');
        sb.append(((this.region == null) ? "<null>" : this.region));
        sb.append(',');
        sb.append("resources");
        sb.append('=');
        sb.append(((this.resources == null) ? "<null>" : this.resources));
        sb.append(',');
        sb.append("detail");
        sb.append('=');
        sb.append(((this.detail == null) ? "<null>" : this.detail));
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
        result = ((result * 31) + ((this.detailType == null) ? 0 : this.detailType.hashCode()));
        result = ((result * 31) + ((this.resources == null) ? 0 : this.resources.hashCode()));
        result = ((result * 31) + ((this.id == null) ? 0 : this.id.hashCode()));
        result = ((result * 31) + ((this.source == null) ? 0 : this.source.hashCode()));
        result = ((result * 31) + ((this.time == null) ? 0 : this.time.hashCode()));
        result = ((result * 31) + ((this.detail == null) ? 0 : this.detail.hashCode()));
        result = ((result * 31) + ((this.additionalProperties == null) ? 0 : this.additionalProperties.hashCode()));
        result = ((result * 31) + ((this.region == null) ? 0 : this.region.hashCode()));
        result = ((result * 31) + ((this.version == null) ? 0 : this.version.hashCode()));
        result = ((result * 31) + ((this.account == null) ? 0 : this.account.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MediaConvertJobStateChangeEvent) == false) {
            return false;
        }
        MediaConvertJobStateChangeEvent rhs = ((MediaConvertJobStateChangeEvent) other);
        return (((((((((((this.detailType == rhs.detailType) || ((this.detailType != null) && this.detailType.equals(rhs.detailType))) && ((this.resources == rhs.resources) || ((this.resources != null) && this.resources.equals(rhs.resources)))) && ((this.id == rhs.id) || ((this.id != null) && this.id.equals(rhs.id)))) && ((this.source == rhs.source) || ((this.source != null) && this.source.equals(rhs.source)))) && ((this.time == rhs.time) || ((this.time != null) && this.time.equals(rhs.time)))) && ((this.detail == rhs.detail) || ((this.detail != null) && this.detail.equals(rhs.detail)))) && ((this.additionalProperties == rhs.additionalProperties) || ((this.additionalProperties != null) && this.additionalProperties.equals(rhs.additionalProperties)))) && ((this.region == rhs.region) || ((this.region != null) && this.region.equals(rhs.region)))) && ((this.version == rhs.version) || ((this.version != null) && this.version.equals(rhs.version)))) && ((this.account == rhs.account) || ((this.account != null) && this.account.equals(rhs.account))));
    }

}
