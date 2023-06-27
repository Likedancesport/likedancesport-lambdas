package com.likedancesport.controller;

import com.likedancesport.common.dto.full.LecturerDto;
import com.likedancesport.common.dto.preview.LecturerPreview;
import com.likedancesport.common.model.domain.learning.Lecturer;
import com.likedancesport.common.utils.HttpHeadersManager;
import com.likedancesport.common.utils.RestUtils;
import com.likedancesport.request.LecturerUpdateRequest;
import com.likedancesport.service.ILecturerService;
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
@RequestMapping("api/lecturers")
public class LecturerController extends AbstractController {
    private final ILecturerService lecturerService;

    @Autowired
    public LecturerController(ILecturerService lecturerService, HttpHeadersManager httpHeadersManager) {
        super(httpHeadersManager);
        this.lecturerService = lecturerService;
    }

    @PostMapping
    public ResponseEntity<LecturerDto> createLecturer(Lecturer lecturer, UriComponentsBuilder uriComponentsBuilder) {
        Lecturer persistedLecturer = lecturerService.createLecturer(lecturer);
        HttpHeaders headers = httpHeadersManager.generateUploadHeaders(lecturer);
        URI lecturerUri = RestUtils.buildUri(uriComponentsBuilder, "api", "lecturers", persistedLecturer.getId().toString());
        return ResponseEntity.created(lecturerUri).headers(headers).body(LecturerDto.of(lecturer));
    }

    @PutMapping("/{lecturerId}")
    public ResponseEntity<LecturerDto> updateLecturer(@PathVariable(name = "lecturerId") Long lecturerId,
                                                      @RequestBody LecturerUpdateRequest lecturerUpdateRequest) {
        Lecturer lecturer = lecturerService.updateLecturer(lecturerId, lecturerUpdateRequest);
        HttpHeaders headers = httpHeadersManager.generateUploadHeaders(lecturer);
        return ResponseEntity.ok()
                .headers(headers)
                .body(LecturerDto.of(lecturer));
    }

    @GetMapping
    public ResponseEntity<Page<LecturerPreview>> getLecturers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Lecturer> lecturers = lecturerService.findAllPaginated(pageable);
        Page<LecturerPreview> previews = lecturers.map(LecturerPreview::of);
        return ResponseEntity.ok()
                .body(previews);
    }

    @GetMapping("/{lecturerId}")
    public ResponseEntity<LecturerDto> getLecturer(@PathVariable(name = "lecturerId") Long lecturerId) {
        Lecturer lecturer = lecturerService.getLecturer(lecturerId);
        return ResponseEntity.ok(LecturerDto.of(lecturer));
    }

    @DeleteMapping("/{lecturerId}")
    public ResponseEntity<?> deleteLecturer(@PathVariable(name = "lecturerId") Long lecturerId) {
        lecturerService.delete(lecturerId);
        return ResponseEntity.ok().build();
    }
}
