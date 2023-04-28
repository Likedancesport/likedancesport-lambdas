package com.likedancesport.common.dto.preview;

import com.likedancesport.common.model.domain.impl.Video;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class VideoPreview extends BasePreview {
    private final int viewsCount;

    public static VideoPreview of(Video video) {
        return VideoPreview.builder()
                .id(video.getId())
                .title(video.getTitle())
                .previewPhotoS3Key(video.getPreviewPhotoS3Key())
                .viewsCount(video.getViewsCount().intValue())
                .build();

    }
}
