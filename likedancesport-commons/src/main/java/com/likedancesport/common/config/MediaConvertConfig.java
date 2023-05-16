package com.likedancesport.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import software.amazon.awssdk.services.mediaconvert.MediaConvertClient;
import software.amazon.awssdk.services.mediaconvert.model.DescribeEndpointsRequest;
import software.amazon.awssdk.services.mediaconvert.model.DescribeEndpointsResponse;

import java.net.URI;

@Configuration
@Lazy
public class MediaConvertConfig {
    @Bean
    public MediaConvertClient mediaConvertClient() {
        try (MediaConvertClient mc = MediaConvertClient.create()) {
            DescribeEndpointsResponse res = mc
                    .describeEndpoints(DescribeEndpointsRequest.builder().maxResults(20).build());

            String endpointURL = res.endpoints().get(0).url();
            return MediaConvertClient.builder()
                    .region(mc.serviceClientConfiguration().region())
                    .endpointOverride(URI.create(endpointURL))
                    .build();
        }
    }
}
