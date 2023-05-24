package com.likedancesport;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import software.amazon.awscdk.CfnParameter;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

@Component
public class StaticStack extends Stack {
    @Autowired
    public StaticStack(@Nullable ParentStack scope, @Qualifier("stageModifier") CfnParameter stageModifier, @Nullable StackProps props) {
        super(scope, "LikedancesportStaticStack-" + stageModifier.getValueAsString(), props);
    }
}
