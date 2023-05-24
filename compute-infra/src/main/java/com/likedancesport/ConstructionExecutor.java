package com.likedancesport;

import com.likedancesport.service.IServiceConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class ConstructionExecutor {
    private final Stack stack;
    private final List<IServiceConstruct> serviceConstructs;
    private final StackProps stackProps;

    @Autowired
    public ConstructionExecutor(ComputeStack stack, List<IServiceConstruct> serviceConstructs, StackProps stackProps) {
        this.stack = stack;
        this.serviceConstructs = serviceConstructs;
        this.stackProps = stackProps;
    }

    @PostConstruct
    public void execute() {
        serviceConstructs.forEach(serviceConstruct -> serviceConstruct.construct(stack, stackProps));
    }
}
