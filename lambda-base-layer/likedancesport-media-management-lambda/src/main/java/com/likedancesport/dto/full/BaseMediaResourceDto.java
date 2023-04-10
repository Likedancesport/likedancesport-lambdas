package com.likedancesport.dto.full;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public abstract class BaseMediaResourceDto {
    private final long id;
    private final String title;
    private final String description;
    private final String previewPhotoS3Key;
}
