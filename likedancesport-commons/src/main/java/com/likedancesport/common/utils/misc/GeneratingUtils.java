package com.likedancesport.common.utils.misc;

import java.util.UUID;

public class GeneratingUtils {
    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public static String generatePreviewPhotoS3Key() {
        return UUID.randomUUID().toString() + "-preview";
    }
}
