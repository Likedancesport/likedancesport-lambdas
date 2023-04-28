package com.likedancesport.controller;

import com.likedancesport.common.dto.full.VideoDto;
import com.likedancesport.common.model.domain.impl.Video;
import com.likedancesport.common.utils.rest.HttpHeadersManager;
import com.likedancesport.common.utils.rest.RestUtils;
import com.likedancesport.request.VideoUpdateRequest;
import com.likedancesport.service.IVideoService;
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

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/courses/{courseId}/sections/{sectionId}/videos")
public class VideoController extends AbstractController {
    private final IVideoService videoService;

    @Autowired
    public VideoController(IVideoService videoService, HttpHeadersManager httpHeadersManager) {
        super(httpHeadersManager);
        this.videoService = videoService;
    }

    @PostMapping
    public ResponseEntity<Video> createVideo(@PathVariable(name = "sectionId") Long sectionId,
                                             @PathVariable(name = "courseId") Long courseId,
                                             @RequestBody @Valid Video video,
                                             UriComponentsBuilder uriComponentsBuilder) {
        Video persistedVideo = videoService.createVideo(sectionId, video);
        HttpHeaders headers = httpHeadersManager.generateUploadHeaders(persistedVideo);
        URI uri = RestUtils.buildUri(uriComponentsBuilder, "api", "courses", courseId.toString(),
                "sections", sectionId.toString(), "videos", video.getId().toString());
        return ResponseEntity.created(uri).headers(headers).body(video);
    }


    @PutMapping("/{videoId}")
    public ResponseEntity<VideoDto> updateVideo(@PathVariable(name = "videoId") Long videoId, @RequestBody VideoUpdateRequest updateRequest) {
        Video video = videoService.updateVideo(videoId, updateRequest);
        HttpHeaders headers = httpHeadersManager.generateUploadHeaders(video);
        return ResponseEntity.ok().headers(headers).body(VideoDto.of(video));
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<?> deleteVideo(@PathVariable(name = "videoId") Long videoId) {
        videoService.delete(videoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<VideoDto> getVideo(@PathVariable(name = "videoId") Long videoId) {
        Video video = videoService.findById(videoId);
        return ResponseEntity.ok(VideoDto.of(video));
    }


}
