package com.likedancesport.config;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

public abstract class AbstractCdkConfig {
    protected final Stack stack;
    protected final StackProps stackProps;

    public AbstractCdkConfig(Stack stack, StackProps stackProps) {
        this.stack = stack;
        this.stackProps = stackProps;
    }
}
