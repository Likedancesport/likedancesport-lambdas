package com.likedancesport.controller;

import com.likedancesport.common.model.impl.Course;
import com.likedancesport.dto.preview.impl.CoursePreview;
import com.likedancesport.request.CourseUpdateRequest;
import com.likedancesport.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/courses")
public class CourseController {
    private final ICourseService courseService;

    @Autowired
    public CourseController(ICourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @DeleteMapping("/{courseId}")
    public Object deleteCourse(@PathVariable(name = "courseId") Long courseId) {
        courseService.delete(courseId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public Page<CoursePreview> getCourses(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Course> courses = courseService.findAllPaginated(pageable);
        return courses.map(CoursePreview::fromCourse);
    }

    @PutMapping("/{courseId}")
    public Course updateCourse(@PathVariable(name = "courseId") Long courseId, CourseUpdateRequest updateRequest) {
        return courseService.updateCourse(courseId, updateRequest);
    }
}
