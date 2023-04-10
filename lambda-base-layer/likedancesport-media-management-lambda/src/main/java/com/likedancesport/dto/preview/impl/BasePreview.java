package com.likedancesport.dto.preview.impl;

import com.likedancesport.dto.preview.IResourcePreview;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.net.URL;

@Data
@SuperBuilder
public abstract class BasePreview implements IResourcePreview {
    private final long id;
    private final String title;
    private final String previewPhotoS3Key;
    private final URL resourceUrl;
}
