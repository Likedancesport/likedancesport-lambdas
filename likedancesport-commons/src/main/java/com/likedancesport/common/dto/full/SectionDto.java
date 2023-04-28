package com.likedancesport.common.dto.full;

import com.likedancesport.common.dto.preview.VideoPreview;
import com.likedancesport.common.model.domain.impl.Section;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class SectionDto extends BaseMediaResourceDto {
    private final List<VideoPreview> videos;

    public static SectionDto of(Section section) {
        return SectionDto.builder()
                .id(section.getId())
                .title(section.getTitle())
                .description(section.getDescription())
                .previewPhotoS3Key(section.getPreviewPhotoS3Key())
                .videos(section.getVideos().stream()
                        .map(VideoPreview::of)
                        .collect(Collectors.toList()))
                .build();
    }
}
