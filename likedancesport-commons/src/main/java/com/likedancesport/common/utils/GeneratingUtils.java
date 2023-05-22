package com.likedancesport.common.utils;

import java.util.UUID;

public class GeneratingUtils {
    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public static String generatePreviewPhotoS3Key() {
        return UUID.randomUUID().toString() + "-preview";
    }
}
