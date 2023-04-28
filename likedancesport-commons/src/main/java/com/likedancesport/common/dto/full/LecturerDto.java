package com.likedancesport.common.dto.full;

import com.likedancesport.common.dto.preview.CoursePreview;
import com.likedancesport.common.model.domain.impl.Lecturer;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class LecturerDto {
    private final long id;
    private final String name;
    private final String avatarS3Key;
    private final List<CoursePreview> courses;

    public static LecturerDto of(Lecturer lecturer) {
        return LecturerDto.builder()
                .id(lecturer.getId())
                .avatarS3Key(lecturer.getPhotoS3Key())
                .name(lecturer.getName())
                .courses(lecturer.getCourses().stream()
                        .map(CoursePreview::of)
                        .collect(Collectors.toList()))
                .build();
    }
}
