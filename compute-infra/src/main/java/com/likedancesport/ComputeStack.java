package com.likedancesport;

import com.likedancesport.service.IServiceConstruct;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import software.amazon.awscdk.App;
import software.amazon.awscdk.CfnParameter;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.List;

@Component
public class ComputeStack extends Stack {
    private final StackProps stackProps;

    @Autowired
    public ComputeStack(@Nullable App scope, @Nullable StackProps props) {
        super(scope, "LikedancesportComputeStack-" + "DEV", props);
        this.stackProps = props;
    }

    public void construct(List<IServiceConstruct> serviceConstructs) {
        serviceConstructs.forEach(serviceConstruct -> serviceConstruct.construct(this, stackProps));
    }
}
