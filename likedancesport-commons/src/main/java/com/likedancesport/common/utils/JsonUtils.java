package com.likedancesport.common.utils;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.serialization.events.serializers.S3EventSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER;
    private static final S3EventSerializer<S3Event> S3_EVENT_SERIALIZER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        S3_EVENT_SERIALIZER = new S3EventSerializer<>();
    }

    public static <T> T fromJson(String stringRepresentation, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(stringRepresentation, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T> String toJson(T item) {
        try {
            return OBJECT_MAPPER.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static S3Event s3EventFromJson(String s3EventString) {
        return S3_EVENT_SERIALIZER.fromJson(s3EventString);
    }
}
