package com.likedancesport;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

@Component
public class ParentStack extends Stack {
    @Autowired
    public ParentStack(@Nullable App scope, @Nullable StackProps props) {
        super(scope, "LikedancesportParentStack", props);
    }
}
