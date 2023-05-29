package com.likedancesport.temp.stacks.bucket_sub;

import com.likedancesport.temp.stacks.BucketsStack;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.NestedStack;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.events.EventBus;
import software.amazon.awscdk.services.events.IEventBus;
import software.constructs.Construct;

@Configuration
public class EventBridgeStack extends NestedStack {

    @Autowired
    public EventBridgeStack(BucketsStack scope) {
        super(scope, "EventBridgeStack");
    }


    @Bean(name = "likedancesportEventBus")
    public IEventBus likedancesportEventBus() {
        return EventBus.Builder.create(this, "likedancesport-event-bus")
                .eventBusName("likedancesport-event-bus")
                .build();
    }
}
