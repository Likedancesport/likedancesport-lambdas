package com.likedancesport.common.dto.preview;

import com.likedancesport.common.model.impl.Lecturer;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LecturerPreview {
    private final long id;
    private final String name;
    private final String avatarS3Key;

    public static LecturerPreview of(Lecturer lecturer) {
        return LecturerPreview.builder()
                .id(lecturer.getId())
                .name(lecturer.getName())
                .avatarS3Key(lecturer.getPhotoS3Key())
                .build();
    }
}
