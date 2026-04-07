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

public final class DesktopServerStatusAdapter implements JsonSerializer<DesktopServerStatus>, JsonDeserializer<DesktopServerStatus> {
    @Override
    public JsonElement serialize(DesktopServerStatus source, Type type, JsonSerializationContext context) {
        return source == null ? JsonNull.INSTANCE : new JsonPrimitive(source.value());
    }

    @Override
    public DesktopServerStatus deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return DesktopServerStatus.NONE;
        }

        return DesktopServerStatus.fromValue(json.getAsInt());
    }
}
