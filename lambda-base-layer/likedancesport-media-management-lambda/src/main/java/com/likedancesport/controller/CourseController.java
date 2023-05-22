package com.likedancesport.controller;

import com.likedancesport.common.dto.full.CourseDto;
import com.likedancesport.common.dto.preview.CoursePreview;
import com.likedancesport.common.model.domain.learning.Course;
import com.likedancesport.common.utils.HttpHeadersManager;
import com.likedancesport.common.utils.RestUtils;
import com.likedancesport.request.CourseUpdateRequest;
import com.likedancesport.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/courses")
public class CourseController extends AbstractController {
    private final ICourseService courseService;

    @Autowired
    public CourseController(ICourseService courseService, HttpHeadersManager httpHeadersManager) {
        super(httpHeadersManager);
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody Course course, UriComponentsBuilder uriComponentsBuilder) {
        Course persistedCourse = courseService.createCourse(course);
        HttpHeaders headers = httpHeadersManager.generateUploadHeaders(persistedCourse);
        URI courseUri = RestUtils.buildUri(uriComponentsBuilder, "api", "courses", persistedCourse.getId().toString());
        return ResponseEntity.created(courseUri).headers(headers).body(CourseDto.of(persistedCourse));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(name = "courseId") Long courseId) {
        courseService.delete(courseId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<CoursePreview>> getCourses(int pageNumber, int pageSize, UriComponentsBuilder uriComponentsBuilder) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Course> courses = courseService.findAllPaginated(pageable);
        Page<CoursePreview> previews = courses.map(CoursePreview::of);
        return ResponseEntity.ok().body(previews);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable(name = "courseId") Long courseId, CourseUpdateRequest updateRequest) {
        Course course = courseService.updateCourse(courseId, updateRequest);
        return ResponseEntity.ok().body(CourseDto.of(course));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable(name = "courseId") Long courseId) {
        Course course = courseService.findById(courseId);
        return ResponseEntity.ok(CourseDto.of(course));
    }
}
