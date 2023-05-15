package com.likedancesport.common.dto.full;

import com.likedancesport.common.model.domain.S3Key;
import com.likedancesport.common.model.domain.learning.Video;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class VideoDto extends TaggableMediaResourceDto {
    private final int viewsCount;
    private final S3Key videoS3Key;

    public static VideoDto of(Video video) {
        return VideoDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .tags(video.getTags())
                .previewPhotoS3Key(video.getPreviewPhotoS3Key())
                .videoS3Key(video.getMp4AssetS3Key())
                .description(video.getDescription())
                .viewsCount(video.getViewsCount().intValue())
                .build();
    }
}
