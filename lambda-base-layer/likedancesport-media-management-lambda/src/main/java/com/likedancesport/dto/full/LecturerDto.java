package com.likedancesport.dto.full;

import com.likedancesport.dto.preview.impl.CoursePreview;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class LecturerDto {
    private final long id;
    private final String name;
    private final String avatarS3Key;
    private final List<CoursePreview> courses;
}
