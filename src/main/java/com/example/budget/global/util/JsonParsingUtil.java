package com.example.budget.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonParsingUtil {

    private JsonParsingUtil() {
        throw new IllegalStateException("Utility class");}

    public static JsonObject parsingToJson(Object jsonString) {
        JsonObject result = null;

        try {
            String json = new ObjectMapper().writeValueAsString(jsonString);
            JsonArray jsonArray = new Gson().fromJson(json, JsonObject.class)
                    .getAsJsonObject("result")
                    .getAsJsonArray("list");

            if (!jsonArray.isEmpty()) {
                result = jsonArray.get(0).getAsJsonObject();
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static JsonArray parsingJsonArray(Object jsonString) {
        JsonArray result = null;

        try {
            String json = new ObjectMapper().writeValueAsString(jsonString);
            JsonArray jsonArray = new Gson().fromJson(json, JsonObject.class)
                    .getAsJsonObject("result")
                    .getAsJsonArray("list");

            if (!jsonArray.isEmpty()) {
                result = jsonArray;
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
