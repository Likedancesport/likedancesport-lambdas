package com.likedancesport;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

@Component
public class ComputeServerlessStack extends Stack {
    @Autowired
    public ComputeServerlessStack(final App scope, final String id, @NotNull final StackProps props) {
        super(scope, id, props);
    }
}
