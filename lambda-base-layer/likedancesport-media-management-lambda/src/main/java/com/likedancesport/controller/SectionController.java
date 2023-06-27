package com.likedancesport.controller;

import com.likedancesport.common.dto.full.SectionDto;
import com.likedancesport.common.model.domain.learning.Section;
import com.likedancesport.common.utils.HttpHeadersManager;
import com.likedancesport.common.utils.RestUtils;
import com.likedancesport.request.SectionUpdateRequest;
import com.likedancesport.service.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/courses/{courseId}/sections")
public class SectionController extends AbstractController {
    private final ISectionService sectionService;

    @Autowired
    public SectionController(ISectionService sectionService, HttpHeadersManager httpHeadersManager) {
        super(httpHeadersManager);
        this.sectionService = sectionService;
    }

    @PutMapping("/{sectionId}")
    public ResponseEntity<SectionDto> updateSection(@PathVariable(name = "sectionId") Long sectionId, @RequestBody SectionUpdateRequest updateRequest) {
        Section section = sectionService.updateSection(sectionId, updateRequest);
        HttpHeaders headers = httpHeadersManager.generateUploadHeaders(section);

        return ResponseEntity.ok().headers(headers).body(SectionDto.of(section));
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
    public ResponseEntity<SectionDto> getSection(@PathVariable(name = "sectionId") Long sectionId) {
        Section section = sectionService.findById(sectionId);
        return ResponseEntity.ok().body(SectionDto.of(section));
    }
}
