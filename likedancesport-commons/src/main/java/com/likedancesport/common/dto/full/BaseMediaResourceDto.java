package com.likedancesport.common.dto.full;

import com.likedancesport.common.model.domain.S3Key;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public abstract class BaseMediaResourceDto {
    private final long id;
    private final String title;
    private final String description;
    private final S3Key previewPhotoS3Key;
}
