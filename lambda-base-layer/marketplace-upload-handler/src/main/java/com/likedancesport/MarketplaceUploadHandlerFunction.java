package com.likedancesport;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class MarketplaceUploadHandlerFunction implements Function<SQSEvent, Void> {
    @Override
    public Void apply(SQSEvent sqsEvent) {
        return null;
    }
}
