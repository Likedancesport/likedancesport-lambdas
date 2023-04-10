package com.likedancesport.dto.preview.impl;

import com.likedancesport.dto.preview.IResourcePreview;
import lombok.Builder;
import lombok.Data;

import java.net.URL;

@Builder
@Data
public class LecturerPreview implements IResourcePreview {
    private final long id;
    private final String name;
    private final String avatarS3Key;
    private final URL resourceUrl;
}
