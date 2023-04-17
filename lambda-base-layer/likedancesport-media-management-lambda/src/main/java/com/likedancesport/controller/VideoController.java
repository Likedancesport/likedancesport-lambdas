package com.likedancesport.controller;

import com.likedancesport.common.model.impl.Video;
import com.likedancesport.common.parameter.annotation.InjectSsmParameter;
import com.likedancesport.common.service.storage.S3StorageService;
import com.likedancesport.request.VideoUpdateRequest;
import com.likedancesport.service.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.net.URL;

@RestController
@RequestMapping("/api/courses/{courseId}/sections/{sectionId}/videos")
public class VideoController {
    private final IVideoService videoService;
    private final S3StorageService s3StorageService;

    @InjectSsmParameter(parameterName = "s3-bucket-name")
    private String videoBucketName;

    @Autowired
    public VideoController(IVideoService videoService, S3StorageService s3StorageService) {
        this.videoService = videoService;
        this.s3StorageService = s3StorageService;
    }

    @PostMapping
    public ResponseEntity<Video> createVideo(@PathVariable(name = "sectionId") Long sectionId, @RequestBody Video video) {
        Video persistedVideo = videoService.createVideo(sectionId, video);
        URL presignedVideoUploadUrl = s3StorageService.generatePresingedUploadUrl(persistedVideo.getVideoS3Key(), videoBucketName);
        URL presignedPhotoUploadUrl = s3StorageService.generatePresingedUploadUrl(persistedVideo.getPreviewPhotoS3Key(), videoBucketName);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("mp4-upload", presignedVideoUploadUrl.toString());
        headers.add("photo-upload", presignedPhotoUploadUrl.toString());
        return new ResponseEntity<>(persistedVideo, headers, HttpStatus.CREATED);
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
