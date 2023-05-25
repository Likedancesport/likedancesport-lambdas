package com.likedancesport.common.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class S3Key {
    /**
     * This is used to extract bucket name and key from S3 URI, first group corresponds to bucket name, second - to key
     */
    private static final Pattern S3_URI_PATTERN = Pattern.compile("^s3://([^/]+)/(\\S*)$");

    @Column(name = "bucket_name")
    private String bucketName;
    @Column(name = "key")
    private String key;

    @JsonIgnore
    public String getUri() {
        return String.format("s3://%s/%s", bucketName, key);
    }

    public static S3Key ofUri(String s3Uri) {
        Matcher matcher = S3_URI_PATTERN.matcher(s3Uri);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid S3 URI");
        }
        String bucketName = matcher.group(1);
        String key = matcher.group(2);

        return S3Key.of(bucketName, key);
    }
}
