package com.likedancesport.controller;

import com.likedancesport.common.model.impl.Video;
import com.likedancesport.common.parameter.annotation.InjectSsmParameter;
import com.likedancesport.common.service.storage.S3StorageService;
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
import java.net.URL;

@RestController
@RequestMapping("/api/courses/{courseId}/sections/{sectionId}/videos")
public class VideoController {
    private final IVideoService videoService;
    private final S3StorageService s3StorageService;
    @InjectSsmParameter(parameterName = "mp4-bucket-name", encrypted = true)
    private String videoBucketName;

    @InjectSsmParameter(parameterName = "thumbnails-bucket-name", encrypted = true)
    private String thumbnailsBucketName;

    @Autowired
    public VideoController(IVideoService videoService, S3StorageService s3StorageService) {
        this.videoService = videoService;
        this.s3StorageService = s3StorageService;
    }

    @PostMapping
    public ResponseEntity<Video> createVideo(@PathVariable(name = "sectionId") Long sectionId,
                                             @PathVariable(name = "courseId") Long courseId,
                                             @RequestBody @Valid Video video,
                                             UriComponentsBuilder uriComponentsBuilder) {
        Video persistedVideo = videoService.createVideo(sectionId, video);
        URL presignedVideoUploadUrl = s3StorageService.generatePresingedUploadUrl(persistedVideo.getVideoS3Key(), videoBucketName);
        URL presignedPhotoUploadUrl = s3StorageService.generatePresingedUploadUrl(persistedVideo.getPreviewPhotoS3Key(), videoBucketName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("mp4-upload", presignedVideoUploadUrl.toString());
        headers.add("photo-upload", presignedPhotoUploadUrl.toString());
        URI uri = RestUtils.buildUri(uriComponentsBuilder, "api", "courses", courseId.toString(),
                "sections", sectionId.toString(), "videos", video.getId().toString());
        return ResponseEntity.created(uri).headers(headers).body(video);
    }

    @PutMapping("/{videoId}")
    public Video updateVideo(@PathVariable(name = "videoId") Long videoId, @RequestBody VideoUpdateRequest updateRequest) {
        return videoService.updateVideo(videoId, updateRequest);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<?> deleteVideo(@PathVariable(name = "videoId") Long videoId) {
        videoService.delete(videoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{videoId}")
    public Video getVideo(@PathVariable(name = "videoId") Long videoId) {
        return videoService.findById(videoId);
    }
}
