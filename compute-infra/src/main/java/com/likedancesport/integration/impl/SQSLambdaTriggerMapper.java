package com.likedancesport.integration.impl;

import com.likedancesport.integration.ILambdaTriggerMapper;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSource;
import software.amazon.awscdk.services.sqs.Queue;

public abstract class SQSLambdaTriggerMapper implements ILambdaTriggerMapper {
    protected final IFunction function;
    protected final Queue queue;

    public SQSLambdaTriggerMapper(IFunction function, Queue queue) {
        this.function = function;
        this.queue = queue;
    }

    @Override
    public void mapLambdas() {
        function.addEventSource(SqsEventSource.Builder.create(queue)
                .maxConcurrency(3)
                .build());
    }
}
