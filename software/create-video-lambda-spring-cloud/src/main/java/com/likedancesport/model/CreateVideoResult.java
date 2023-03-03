package com.likedancesport.model;

import com.likedancesport.common.model.impl.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@Builder
@Data
public class CreateVideoResult {
    private final Long id;
    private final String title;
    private final String description;
    private final Long durationSeconds;
    private final String s3Key;
    private final URL presignedUrl;
    private final Set<Tag> tags;
}
