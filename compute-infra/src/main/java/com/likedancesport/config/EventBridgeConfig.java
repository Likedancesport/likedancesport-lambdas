package com.likedancesport.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.events.EventBus;
import software.amazon.awscdk.services.events.IEventBus;

@Configuration
public class EventBridgeConfig extends AbstractCdkConfig {
    @Autowired
    public EventBridgeConfig(Stack stack, StackProps stackProps) {
        super(stack, stackProps);
    }

    @Bean(name = "likedancesportEventBus")
    public IEventBus likedancesportEventBus() {
        return EventBus.Builder.create(stack, "likedancesport-event-bus")
                .eventBusName("likedancesport-event-bus")
                .build();
    }
}
