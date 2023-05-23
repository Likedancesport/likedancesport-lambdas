package com.likedancesport.common.utils;

public class StringUtils {
    public static String replaceInlineWhitespacesWith(String replacement, String str) {
        return RegexPatterns.WHITESPACE_PATTERN.matcher(str).replaceAll(replacement);
    }


}
