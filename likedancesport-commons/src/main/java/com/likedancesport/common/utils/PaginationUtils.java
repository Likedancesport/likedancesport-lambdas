package com.likedancesport.common.utils;

import org.springframework.data.domain.Pageable;

public class PaginationUtils {
    public static boolean isValidPage(long totalCount, Pageable pageable) {
        return (long) pageable.getPageNumber() * pageable.getPageSize() < totalCount;
    }
}
