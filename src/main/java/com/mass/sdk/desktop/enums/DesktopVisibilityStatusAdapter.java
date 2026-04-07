package com.mass.sdk.desktop.enums;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public final class DesktopVisibilityStatusAdapter implements JsonSerializer<DesktopVisibilityStatus>, JsonDeserializer<DesktopVisibilityStatus> {
    @Override
    public JsonElement serialize(DesktopVisibilityStatus source, Type type, JsonSerializationContext context) {
        return source == null ? JsonNull.INSTANCE : new JsonPrimitive(source.value());
    }

    @Override
    public DesktopVisibilityStatus deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return DesktopVisibilityStatus.PUBLIC;
        }

        return DesktopVisibilityStatus.fromValue(json.getAsInt());
    }
}
