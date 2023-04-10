package com.likedancesport.dto.full;

import com.likedancesport.dto.preview.impl.VideoPreview;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class SectionDto extends BaseMediaResourceDto {
    private final List<VideoPreview> videos;
}
