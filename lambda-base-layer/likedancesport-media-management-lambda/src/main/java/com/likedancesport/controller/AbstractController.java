package com.likedancesport.controller;

import com.likedancesport.common.utils.HttpHeadersManager;

public abstract class AbstractController {
    protected final HttpHeadersManager httpHeadersManager;

    public AbstractController(HttpHeadersManager httpHeadersManager) {
        this.httpHeadersManager = httpHeadersManager;
    }
}
