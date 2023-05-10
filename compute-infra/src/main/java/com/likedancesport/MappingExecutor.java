package com.likedancesport;

import com.likedancesport.integration.ILambdaTriggerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class MappingExecutor {
    private final List<ILambdaTriggerMapper> mappers;

    @Autowired
    public MappingExecutor(List<ILambdaTriggerMapper> mappers) {
        this.mappers = mappers;
    }

    @PostConstruct
    public void execute(){
        mappers.forEach(ILambdaTriggerMapper::mapLambdas);
    }
}
