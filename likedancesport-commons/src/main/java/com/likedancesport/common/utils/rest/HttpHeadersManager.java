package com.likedancesport.common.utils.rest;

import com.likedancesport.common.model.domain.IPreviewable;
import com.likedancesport.common.model.domain.impl.Video;
import com.likedancesport.common.parameter.annotation.InjectSsmParameter;
import com.likedancesport.common.service.storage.S3StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;

@Component
public class HttpHeadersManager {
    private final S3StorageService s3StorageService;

    @InjectSsmParameter(parameterName = "mp4-bucket-name", encrypted = true)
    private String videoBucketName;

    @InjectSsmParameter(parameterName = "thumbnails-bucket-name", encrypted = true)
    private String thumbnailsBucketName;

    @Autowired
    public HttpHeadersManager(S3StorageService s3StorageService) {
        this.s3StorageService = s3StorageService;
    }

    public HttpHeaders generateUploadHeaders(IPreviewable previewableEntity) {
        HttpHeaders headers = new HttpHeaders();
        URL presignedPhotoUploadUrl = s3StorageService.generatePresingedUploadUrl(previewableEntity.getPhotoS3Key(), thumbnailsBucketName);
        headers.add("preview-upload", presignedPhotoUploadUrl.toString());
        return headers;
    }

    private HttpHeaders generateUploadHeaders(Video video) {
        HttpHeaders headers = generateUploadHeaders((IPreviewable) video);
        URL presignedVideoUploadUrl = s3StorageService.generatePresingedUploadUrl(video.getVideoS3Key(), videoBucketName);
        headers.add("mp4-upload", presignedVideoUploadUrl.toString());
        return headers;
    }

    private class HttpHeadersChain implements AutoCloseable {
        private final HttpHeaders headers;

        private HttpHeadersChain() {
            this.headers = new HttpHeaders();
        }

        private HttpHeadersChain(HttpHeaders headers) {
            this.headers = headers;
        }

        public HttpHeadersChain addHeader(String name, String value) {
            headers.add(name, value);
            return this;
        }

        public HttpHeadersChain addHeader(String name, Object value) {
            headers.add(name, value.toString());
            return this;
        }

        public HttpHeadersChain uploads(IPreviewable previewable) {
            URL presignedPhotoUploadUrl = s3StorageService.generatePresingedUploadUrl(previewable.getPhotoS3Key(), thumbnailsBucketName);
            headers.add("preview-upload", presignedPhotoUploadUrl.toString());
            return this;
        }

        public HttpHeadersChain uploads(Video video) {
            uploads((IPreviewable) video);
            URL presignedVideoUploadUrl = s3StorageService.generatePresingedUploadUrl(video.getVideoS3Key(), videoBucketName);
            headers.add("mp4-upload", presignedVideoUploadUrl.toString());
            return this;
        }

        public HttpHeadersChain pagination(Page<?> page, UriComponentsBuilder uriComponentsBuilder, String... pathSegments) {
            headers.addAll(RestUtils.configureHttpHeadersForPage(page, uriComponentsBuilder, pathSegments));
            return this;
        }

        public HttpHeaders compose() {
            HttpHeaders result = HttpHeaders.readOnlyHttpHeaders(headers);
            close();
            return result;
        }

        @Override
        public void close() {
            headers.clear();
        }
    }

}
