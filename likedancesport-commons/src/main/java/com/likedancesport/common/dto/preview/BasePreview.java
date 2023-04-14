package com.likedancesport.common.dto.preview;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class BasePreview {
    private final long id;
    private final String title;
    private final String previewPhotoS3Key;
}
