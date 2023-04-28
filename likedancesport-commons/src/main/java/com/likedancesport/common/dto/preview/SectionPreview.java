package com.likedancesport.common.dto.preview;

import com.likedancesport.common.model.domain.impl.Section;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class SectionPreview extends BasePreview {
    private final int videosCount;

    public static SectionPreview of(Section section) {
        return SectionPreview.builder()
                .id(section.getId())
                .title(section.getTitle())
                .previewPhotoS3Key(section.getPreviewPhotoS3Key())
                .videosCount(section.getVideos().size())
                .build();
    }
}
