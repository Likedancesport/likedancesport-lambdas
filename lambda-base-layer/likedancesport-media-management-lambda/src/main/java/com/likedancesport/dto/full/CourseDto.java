package com.likedancesport.dto.full;

import com.likedancesport.dto.preview.impl.LecturerPreview;
import com.likedancesport.dto.preview.impl.SectionPreview;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseDto extends TaggableMediaResourceDto {
    private final List<SectionPreview> sections;
    private final List<LecturerPreview> lecturers;
}
