package com.likedancesport.common.utils.misc;

import com.likedancesport.common.utils.constants.RegexPatterns;

public class StringUtils {
    public static String replaceInlineWhitespacesWith(String replacement, String str) {
        return RegexPatterns.WHITESPACE_PATTERN.matcher(str).replaceAll(replacement);
    }

}
