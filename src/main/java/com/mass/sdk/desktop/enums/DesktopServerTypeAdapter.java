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
import java.util.Locale;

public final class DesktopServerTypeAdapter implements JsonSerializer<DesktopServerType>, JsonDeserializer<DesktopServerType> {
    @Override
    public JsonElement serialize(DesktopServerType source, Type type, JsonSerializationContext context) {
        return source == null ? JsonNull.INSTANCE : new JsonPrimitive(source.value());
    }

    @Override
    public DesktopServerType deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return DesktopServerType.DOCKER;
        }

        return DesktopServerType.fromValue(json.getAsString().toLowerCase(Locale.ROOT));
    }
}
