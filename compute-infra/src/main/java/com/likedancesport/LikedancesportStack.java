package com.likedancesport;

import com.likedancesport.service.IServiceConstruct;
import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.List;

public class LikedancesportStack extends Stack {
    private final StackProps stackProps;

    public LikedancesportStack(@Nullable App scope, @Nullable String modifier, @Nullable StackProps props) {
        super(scope, "LikedancesportStack-" + modifier, props);
        this.stackProps = props;
    }

    public void construct(List<IServiceConstruct> serviceConstructs) {
        serviceConstructs.forEach(serviceConstruct -> serviceConstruct.construct(this, stackProps));
    }
}
