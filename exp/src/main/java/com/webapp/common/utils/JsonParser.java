package com.webapp.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Type;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonParser {

    private static final String EQUALS_UTF_CODE = "\\u003d";

    private static Gson gsonWithNulls = new GsonBuilder().serializeNulls().create();
    private static Gson gson = new GsonBuilder().create();
    private static Gson prettyPrintingGson = new GsonBuilder().setPrettyPrinting().create();
    private static Gson printingFullGson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private static com.google.gson.JsonParser jsonParser = new com.google.gson.JsonParser();
    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object obj) {
        return toJson(obj, true);
    }

    private static String toJson(Object obj, boolean serializeNulls) {
        String json;
        if (serializeNulls) {
            json = gsonWithNulls.toJson(obj);
        } else {
            json = gson.toJson(obj);
        }
        /*
         * Escaping equals sign is not necessary
         */
        return json.replace(EQUALS_UTF_CODE, StrUtil.EQUAL_SIGN);
    }

    public static String toPrintingFullJson(Object obj) {
        return printingFullGson.toJson(obj);
    }

    public static String toPrettyPrintingJson(Object obj) {
        return prettyPrintingGson.toJson(obj);
    }

    @Deprecated
    public static <T> T fromJson(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    @Deprecated
    public static <T> T toPojo(Object object, Class<T> clazz) {
        JsonElement jsonElement = gson.toJsonTree(object);
        return gson.fromJson(jsonElement, clazz);
    }

    public static Map toMap(Object object) {
        String json = toJson(object);
        return gson.fromJson(json, Map.class);
    }

    @Deprecated
    public static <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }

    public static boolean isJson(String json) {
        try {
            gson.fromJson(json, Object.class);
            return true;
        } catch (com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

    public static JsonObject getJsonObject(Object src) {
        return (JsonObject) getJsonElement(gson.toJson(src));
    }

    public static JsonElement getJsonElement(String json) {
        return jsonParser.parse(json);
    }

    @SneakyThrows
    public static String toJsonViaJackson(Object object) {
        return toJsonViaJackson(object, false);
    }

    @SneakyThrows
    public static String toJsonViaJackson(Object object, boolean ignoreNull) {
        if (ignoreNull) {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        return mapper.writeValueAsString(object);
    }

    @SneakyThrows
    public static <T> T fromJsonViaJackson(String json, Class<T> type) {
        return mapper.readValue(json, type);
    }
}