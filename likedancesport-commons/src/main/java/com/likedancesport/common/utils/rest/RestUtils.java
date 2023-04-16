package com.likedancesport.common.utils.rest;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URL;

public class RestUtils {
    public static URI buildUri(UriComponentsBuilder uriComponentsBuilder, String... pathSegments) {
        return uriComponentsBuilder.
                pathSegment(pathSegments)
                .build()
                .toUri();
    }

    public static String createPaginationHeader(UriComponentsBuilder uriComponentsBuilder, int pageNumber, int pageSize, String... pathSegments) {
        return uriComponentsBuilder.
                pathSegment(pathSegments)
                .queryParam("pageNumber", pageNumber)
                .queryParam("pageSize", pageSize)
                .build()
                .toUriString();
    }

    public static HttpHeaders configureHttpHeadersForPage(Page<?> page, UriComponentsBuilder uriComponentsBuilder,
                                                          String... pathSegments) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (page.getTotalPages() > page.getNumber() + 1) {
            httpHeaders.set("Next page", createPaginationHeader(uriComponentsBuilder.cloneBuilder(),
                    page.getNumber() + 1, page.getSize(), pathSegments));
        }
        if (page.getNumber() != 0) {
            httpHeaders.set("Previous page", createPaginationHeader(uriComponentsBuilder.cloneBuilder(),
                    page.getNumber() - 1, page.getSize(), pathSegments));
        }
        return httpHeaders;
    }
}
