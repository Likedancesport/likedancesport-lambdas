package com.likedancesport.controller;

import com.likedancesport.common.dto.full.LecturerDto;
import com.likedancesport.common.dto.preview.LecturerPreview;
import com.likedancesport.common.model.impl.Lecturer;
import com.likedancesport.common.parameter.annotation.InjectSsmParameter;
import com.likedancesport.common.service.storage.S3StorageService;
import com.likedancesport.common.utils.rest.RestUtils;
import com.likedancesport.request.LecturerUpdateRequest;
import com.likedancesport.service.ILecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("api/lecturers")
public class LecturerController {
    private final ILecturerService lecturerService;
    private final S3StorageService s3StorageService;
    @InjectSsmParameter(parameterName = "thumbnails-bucket-name", encrypted = true)
    private String thumbnailsBucketName;

    @Autowired
    public LecturerController(ILecturerService lecturerService, S3StorageService s3StorageService) {
        this.lecturerService = lecturerService;
        this.s3StorageService = s3StorageService;
    }

    @PostMapping
    public ResponseEntity<LecturerDto> createLecturer(Lecturer lecturer, UriComponentsBuilder uriComponentsBuilder) {
        Lecturer persistedLecturer = lecturerService.createLecturer(lecturer);
        URL avatarUploadUrl = s3StorageService.generatePresingedUploadUrl(persistedLecturer.getPhotoS3Key(), thumbnailsBucketName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("thumbnail-upload", avatarUploadUrl.toString());
        URI lecturerUri = RestUtils.buildUri(uriComponentsBuilder, "api", "lecturers", persistedLecturer.getId().toString());
        return ResponseEntity.created(lecturerUri).headers(headers).body(LecturerDto.of(lecturer));
    }

    @PutMapping("/{lecturerId}")
    public Lecturer updateLecturer(@PathVariable(name = "lecturerId") Long lecturerId,
                                   @RequestBody LecturerUpdateRequest lecturerUpdateRequest) {
        return null;
    }

    @GetMapping
    public Page<LecturerPreview> getLecturers(int pageNumber, int pageSize) {
        return null;
    }

    @GetMapping("/{lecturerId}")
    public LecturerDto getLecturer(@PathVariable(name = "lecturerId") Long lecturerId) {
        return null;
    }

    @DeleteMapping("/{lecturerId}")
    public ResponseEntity<?> deleteLecturer(@PathVariable(name = "lecturerId") Long lecturerId) {
        return null;
    }
}
