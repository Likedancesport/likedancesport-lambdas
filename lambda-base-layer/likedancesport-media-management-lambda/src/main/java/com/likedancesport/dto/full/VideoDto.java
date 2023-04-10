package com.likedancesport.dto.full;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class VideoDto extends BaseMediaResourceDto {
    private final int viewsCount;
    private final String videoS3Key;
}
