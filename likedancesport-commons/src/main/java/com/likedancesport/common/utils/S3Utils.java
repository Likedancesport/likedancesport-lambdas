package com.likedancesport.common.utils;

import com.likedancesport.common.model.domain.S3Key;

public class S3Utils {
    public static String extractFolderFromKey(S3Key s3Key) {
        String key = s3Key.getKey();
        return key.substring(0, key.lastIndexOf("/"));
    }
}
