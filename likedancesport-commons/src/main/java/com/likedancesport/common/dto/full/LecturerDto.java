package com.likedancesport.common.dto.full;

import com.likedancesport.common.dto.preview.CoursePreview;
import com.likedancesport.common.model.domain.S3Key;
import com.likedancesport.common.model.domain.learning.Lecturer;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class LecturerDto {
    private final long id;
    private final String name;
    private final S3Key avatarS3Key;
    private final List<CoursePreview> courses;

    public static LecturerDto of(Lecturer lecturer) {
        return LecturerDto.builder()
                .id(lecturer.getId())
                .name(lecturer.getName())
                .avatarS3Key(lecturer.getPhotoS3Key())
                .courses(lecturer.getCourses().stream()
                        .map(CoursePreview::of)
                        .collect(Collectors.toList()))
                .build();
    }
}
