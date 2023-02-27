package com.likedancesport.utils.misc;

import java.util.UUID;

public class GeneratingUtils {
    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}
