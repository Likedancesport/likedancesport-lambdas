package com.likedancesport.controller;

import com.likedancesport.common.dto.full.CourseDto;
import com.likedancesport.common.dto.preview.CoursePreview;
import com.likedancesport.common.model.impl.Course;
import com.likedancesport.common.parameter.annotation.InjectSsmParameter;
import com.likedancesport.common.service.storage.S3StorageService;
import com.likedancesport.common.utils.rest.RestUtils;
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
import java.net.URL;

@RestController
@RequestMapping("api/courses")
public class CourseController {
    private final ICourseService courseService;
    private final S3StorageService s3StorageService;
    @InjectSsmParameter(parameterName = "thumbnails-bucket-name", encrypted = true)
    private String thumbnailsBucketName;

    @Autowired
    public CourseController(ICourseService courseService, S3StorageService s3StorageService) {
        this.courseService = courseService;
        this.s3StorageService = s3StorageService;
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody Course course, UriComponentsBuilder uriComponentsBuilder) {
        Course persistedCourse = courseService.createCourse(course);
        URL photoUploadUrl = s3StorageService.generatePresingedUploadUrl(persistedCourse.getPreviewPhotoS3Key(), thumbnailsBucketName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("photo-upload", photoUploadUrl.toString());
        URI courseUri = RestUtils.buildUri(uriComponentsBuilder, "api", "courses", persistedCourse.getId().toString());
        return ResponseEntity.created(courseUri).headers(headers).body(CourseDto.of(persistedCourse));
    }

    @DeleteMapping("/{courseId}")
    public Object deleteCourse(@PathVariable(name = "courseId") Long courseId) {
        courseService.delete(courseId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public Page<CoursePreview> getCourses(int pageNumber, int pageSize, UriComponentsBuilder uriComponentsBuilder) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Course> courses = courseService.findAllPaginated(pageable);
        return courses.map(CoursePreview::of);
    }

    @PutMapping("/{courseId}")
    public CourseDto updateCourse(@PathVariable(name = "courseId") Long courseId, CourseUpdateRequest updateRequest) {
        Course course = courseService.updateCourse(courseId, updateRequest);
        return CourseDto.of(course);
    }

    @GetMapping("/{courseId}")
    public CourseDto getCourse(@PathVariable(name = "courseId") Long courseId) {
        Course course = courseService.findById(courseId);
        return CourseDto.of(course);
    }
}
