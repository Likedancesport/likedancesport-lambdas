package com.likedancesport.common.dto.preview;

import com.likedancesport.common.model.domain.S3Key;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class BasePreview {
    private final long id;
    private final String title;
    private final S3Key previewPhotoS3Key;
}
