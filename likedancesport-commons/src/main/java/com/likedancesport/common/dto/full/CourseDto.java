package com.likedancesport.common.dto.full;

import com.likedancesport.common.dto.preview.LecturerPreview;
import com.likedancesport.common.dto.preview.SectionPreview;
import com.likedancesport.common.model.impl.Course;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseDto extends TaggableMediaResourceDto {
    private final List<SectionPreview> sections;
    private final List<LecturerPreview> lecturers;

    public static CourseDto of(Course course) {
        return CourseDto.builder()
                .id(course.getId())
                .tags(course.getTags())
                .description(course.getDescription())
                .previewPhotoS3Key(course.getPreviewPhotoS3Key())
                .lecturers(course.getLecturers().stream()
                        .map(LecturerPreview::of)
                        .collect(Collectors.toList()))
                .sections(course.getSections().stream()
                        .map(SectionPreview::of)
                        .collect(Collectors.toList()))
                .build();
    }
}
