package com.likedancesport.dto.preview.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class VideoPreview extends BasePreview {
    private final int viewsCount;
}
