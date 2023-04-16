package com.likedancesport.controller;

import com.likedancesport.common.dto.full.SectionDto;
import com.likedancesport.common.model.impl.Section;
import com.likedancesport.request.SectionUpdateRequest;
import com.likedancesport.service.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses/{courseId}/sections")
public class SectionController {
    private final ISectionService sectionService;

    @Autowired
    public SectionController(ISectionService sectionService) {
        this.sectionService = sectionService;
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
    public Section addToCourse(@PathVariable(name = "courseId") Long courseId, @RequestBody Section section) {
        return sectionService.createSection(courseId, section);
    }

    @GetMapping("/{sectionId}")
    public SectionDto getSection(@PathVariable(name = "sectionId") Long sectionId) {
        Section section = sectionService.findById(sectionId);
        return SectionDto.of(section);
    }
}
