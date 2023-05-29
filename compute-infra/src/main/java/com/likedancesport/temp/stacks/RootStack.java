package com.likedancesport.temp.stacks;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

@Component
public class RootStack extends Stack {
    @Autowired
    public RootStack(@Nullable App scope, @Nullable StackProps props) {
        super(scope, "LikedancesportRootStack", props);
    }
}
