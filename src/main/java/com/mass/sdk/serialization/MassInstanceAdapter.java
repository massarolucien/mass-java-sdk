package com.mass.sdk.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mass.sdk.models.GameInstance;
import com.mass.sdk.models.MassInstance;
import com.mass.sdk.models.ProxyInstance;

import java.lang.reflect.Type;

public final class MassInstanceAdapter implements JsonSerializer<MassInstance>, JsonDeserializer<MassInstance> {
    @Override
    public JsonElement serialize(MassInstance source, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(source, source == null ? MassInstance.class : source.getClass());
    }

    @Override
    public MassInstance deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return null;
        }

        var targetType = resolveType(json.getAsJsonObject());
        return context.deserialize(json, targetType);
    }

    private Class<? extends MassInstance> resolveType(JsonObject jsonObject) {
        if (jsonObject == null || !jsonObject.has("type")) {
            return MassInstance.class;
        }

        var type = jsonObject.get("type").isJsonNull() ? "" : jsonObject.get("type").getAsString();
        return switch (type) {
            case "java", "cpp" -> GameInstance.class;
            case "java_proxy" -> ProxyInstance.class;
            default -> MassInstance.class;
        };
    }
}
