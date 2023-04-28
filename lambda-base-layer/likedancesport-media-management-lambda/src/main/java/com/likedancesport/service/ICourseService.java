package com.likedancesport.service;

import com.likedancesport.common.model.domain.impl.Course;
import com.likedancesport.request.CourseUpdateRequest;

public interface ICourseService extends ITaggableMediaResourseService<Course> {
    void addLecturerToCourse(Long courseId, Long lecturerId);

    Course createCourse(Course course);

    Course updateCourse(Long courseId, CourseUpdateRequest updateRequest);
}
