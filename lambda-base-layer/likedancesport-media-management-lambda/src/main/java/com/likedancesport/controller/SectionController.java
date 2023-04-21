package com.likedancesport.controller;

import com.likedancesport.common.dto.full.SectionDto;
import com.likedancesport.common.model.impl.Section;
import com.likedancesport.common.parameter.annotation.InjectSsmParameter;
import com.likedancesport.common.service.storage.S3StorageService;
import com.likedancesport.common.utils.rest.RestUtils;
import com.likedancesport.request.SectionUpdateRequest;
import com.likedancesport.service.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/courses/{courseId}/sections")
public class SectionController {
    private final ISectionService sectionService;
    private final S3StorageService s3StorageService;
    @InjectSsmParameter(parameterName = "thumbnails-bucket-name", encrypted = true)
    private String thumbnailsBucketName;

    @Autowired
    public SectionController(ISectionService sectionService, S3StorageService s3StorageService) {
        this.sectionService = sectionService;
        this.s3StorageService = s3StorageService;
    }

    @PutMapping("/{sectionId}")
    public SectionDto updateSection(@PathVariable(name = "sectionId") Long sectionId, @RequestBody SectionUpdateRequest updateRequest) {
        Section section = sectionService.updateSection(sectionId, updateRequest);
        return SectionDto.of(section);
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<Object> deleteSection(@PathVariable(name = "sectionId") Long sectionId) {
        sectionService.delete(sectionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<SectionDto> addToCourse(@PathVariable(name = "courseId") Long courseId, @RequestBody Section section, UriComponentsBuilder uriComponentsBuilder) {
        Section persistedSection = sectionService.createSection(courseId, section);
        SectionDto sectionDto = SectionDto.of(persistedSection);
        URI sectionUri = RestUtils.buildUri(uriComponentsBuilder,
                "api", "courses", courseId.toString(), "sections", persistedSection.getId().toString());
        return ResponseEntity.created(sectionUri).body(sectionDto);
    }

    @GetMapping("/{sectionId}")
    public SectionDto getSection(@PathVariable(name = "sectionId") Long sectionId) {
        Section section = sectionService.findById(sectionId);
        return SectionDto.of(section);
    }
}
