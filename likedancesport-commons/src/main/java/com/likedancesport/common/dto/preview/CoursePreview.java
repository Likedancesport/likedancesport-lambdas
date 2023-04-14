package com.likedancesport.common.dto.preview;

import com.likedancesport.common.model.impl.Course;
import com.likedancesport.common.model.impl.Lecturer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class CoursePreview extends BasePreview {
    private final int sectionsCount;
    private final int videosCount;
    private final List<String> lecturersNames;

    public static CoursePreview of(Course course)  {
        return CoursePreview.builder()
                .id(course.getId())
                .title(course.getTitle())
                .previewPhotoS3Key(course.getPreviewPhotoS3Key())
                .lecturersNames(course.getLecturers().stream()
                        .map(Lecturer::getName)
                        .collect(Collectors.toList()))
                .sectionsCount(course.getSections().size())
                .videosCount(course.getSections().stream()
                        .reduce(0, (integer, section) -> section.getVideos().size(), Integer::sum))
                .build();
    }
}
