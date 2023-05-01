package com.likedancesport.config;

import software.amazon.awscdk.Stack;

public abstract class AbstractCdkConfig {
    protected final Stack stack;

    public AbstractCdkConfig(Stack stack) {
        this.stack = stack;
    }
}
