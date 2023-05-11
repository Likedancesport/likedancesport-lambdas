package com.likedancesport.service;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

public interface IServiceConstruct {
    void construct(Stack stack, StackProps stackProps);
}
