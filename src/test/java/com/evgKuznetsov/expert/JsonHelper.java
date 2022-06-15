package com.evgKuznetsov.expert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.List;

@UtilityClass
public class JsonHelper {

    private static ObjectMapper objectMapper;

    public static void setObjectMapper(ObjectMapper om) {
        JsonHelper.objectMapper = om;
    }

    public static <T> T readValue(String data, Class<T> clazz) {
        try {
            return objectMapper.readValue(data, clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Something wrong on reading JSON");
        }
    }

    public static <T> List<T> readIterable(String data, Class<T> clazz) {
        ObjectReader reader = objectMapper.readerFor(clazz);
        try {
            return reader.<T>readValues(data).readAll();
        } catch (IOException e) {
            throw new IllegalArgumentException("Something wrong on reading JSON");
        }
    }

    public static <T> String getJson(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Something wrong on writing JSON string");
        }
    }
}
