package com.likedancesport.common.utils;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.serialization.events.serializers.S3EventSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likedancesport.common.model.domain.S3Key;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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


    public static List<S3Key> getS3KeysFromS3Event(String source) {
        JsonNode jsonNode = parseStringToNode(source);
        JsonNode recordsNode = jsonNode.get("Records");
        if (recordsNode == null) {
            throw new IllegalArgumentException("Invalid source");
        }
        List<S3Key> s3Keys = new ArrayList<>();
        recordsNode.spliterator().forEachRemaining(recordNode -> s3Keys.add(getS3KeyFromRecordsNode(recordNode)));
        return s3Keys;
    }

    public static S3Key getS3KeyFromRecordsNode(JsonNode recordNode) {
        try {
            JsonNode s3 = recordNode.get("s3");
            String bucketName = s3.get("bucket").get("name").textValue();
            String key = s3.get("object").get("key").textValue();
            return S3Key.of(bucketName, key);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Invalid node");
        }
    }

    public static <T> T parseJsonNode(JsonNode jsonNode, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.treeToValue(jsonNode, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String toJson(T item) {
        try {
            return OBJECT_MAPPER.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static JsonNode parseStringToNode(String source) {
        try {
            return OBJECT_MAPPER.readTree(source);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static S3Event s3EventFromJson(String s3EventString) {
        log.debug("PARSING S3 EVENT");
        return S3_EVENT_SERIALIZER.fromJson(s3EventString);
    }
}
