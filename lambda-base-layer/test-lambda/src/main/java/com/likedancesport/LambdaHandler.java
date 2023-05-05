package com.likedancesport;

import com.likedancesport.common.annotation.InjectSsmParameter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.function.Function;

@Component
public class LambdaHandler implements Function<Object, Object> {
    @InjectSsmParameter(parameterName = "test-parameter")
    private String r;

    @Override
    public Object apply(Object o) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return r;
    }

    @PostConstruct
    public void post() {
        System.out.println("---- PARAMETER: " + r);
    }
}
