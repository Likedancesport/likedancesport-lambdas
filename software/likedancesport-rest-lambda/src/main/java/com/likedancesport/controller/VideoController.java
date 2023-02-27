package com.likedancesport.controller;

import com.likedancesport.model.impl.Video;
import com.likedancesport.service.IVideoService;
import dev.ponomarenko.util.rest.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/videos")
public class VideoController {
    private static final String CONTROLLER_ROOT_PATH_SEGMENT = VideoController.class.getAnnotation(RequestMapping.class).path()[0];
    private final IVideoService videoService;

    @Autowired
    public VideoController(IVideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping
    public ResponseEntity<Page<Video>> getVideos(int pageNumber, int pageSize, UriComponentsBuilder uriComponentsBuilder) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Video> videoPage = videoService.getVideosPaginated(pageable);
        HttpHeaders httpHeaders = RestUtils.configureHttpHeadersForPage(videoPage, uriComponentsBuilder, CONTROLLER_ROOT_PATH_SEGMENT);
        return new ResponseEntity<>(videoPage, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideo(UUID id) {
        return null;
    }
}
