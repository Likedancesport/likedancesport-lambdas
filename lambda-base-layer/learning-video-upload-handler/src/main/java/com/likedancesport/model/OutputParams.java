package com.likedancesport.model;

import com.likedancesport.common.utils.MediaConvertUtils;
import software.amazon.awssdk.services.mediaconvert.model.Output;

public enum OutputParams {
    _1080P("1080p", "_1080p", 3500000, 8, 1920, 1080),
    _720P("720p", "720p", 1200000, 7, 1280, 720);
    private final String nameModifier;
    private final String segmentModifier;
    private final int qvbrMaxBitrate;
    private final int qvbrQualityLevel;
    private final int targetWidth;
    private final int targetHeight;

    OutputParams(String nameModifier, String segmentModifier, int qvbrMaxBitrate, int qvbrQualityLevel,
                 int targetWidth, int targetHeight) {
        this.nameModifier = nameModifier;
        this.segmentModifier = segmentModifier;
        this.qvbrMaxBitrate = qvbrMaxBitrate;
        this.qvbrQualityLevel = qvbrQualityLevel;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
    }

    public Output buildOutput() {
        return MediaConvertUtils.createOutput(nameModifier, segmentModifier, qvbrMaxBitrate, qvbrQualityLevel, targetWidth, targetHeight);
    }
}
