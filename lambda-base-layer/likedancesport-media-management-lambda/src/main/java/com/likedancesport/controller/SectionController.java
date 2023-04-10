package com.likedancesport.controller;

import com.likedancesport.common.model.impl.Section;
import com.likedancesport.request.SectionUpdateRequest;
import com.likedancesport.service.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses/{courseId}/sections")
public class SectionController {
    private final ISectionService sectionService;

    @Autowired
    public SectionController(ISectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PutMapping("/{sectionId}")
    public Section updateSection(@PathVariable(name = "sectionId") Long sectionId, @RequestBody SectionUpdateRequest updateRequest) {
        return sectionService.updateSection(sectionId, updateRequest);
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
}
