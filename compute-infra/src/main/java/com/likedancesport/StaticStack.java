package com.likedancesport;

import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

public class StaticStack extends Stack {
    public StaticStack(@Nullable App scope, @Nullable String modifier, @Nullable StackProps props) {
        super(scope, "LikedancesportStaticStack-" + modifier, props);
    }
}
