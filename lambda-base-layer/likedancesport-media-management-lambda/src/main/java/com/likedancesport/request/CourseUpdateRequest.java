package com.likedancesport.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CourseUpdateRequest extends TaggableMediaResourceUpdateRequest {
    /**
     * Used to specify order of sections in the course
     * ID of section is mapped to its order in course (ID -> Order)
     */
    private Map<Long,Integer> sectionOrderingMap;
}
