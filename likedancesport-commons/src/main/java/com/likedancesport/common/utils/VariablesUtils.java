package com.likedancesport.common.utils;

import org.springframework.lang.Nullable;

public class VariablesUtils {
    public static boolean notAllSpecified(@Nullable Object... objects) {
        if (objects == null || objects.length == 0) {
            return true;
        }
        for (Object object : objects) {
            if (object == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean stringIsNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
