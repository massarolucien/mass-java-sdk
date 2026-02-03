package com.mass.sdk.desktop.enums;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class DesktopGameVersionAdapter implements JsonSerializer<DesktopGameVersion>, JsonDeserializer<DesktopGameVersion> {

    @Override
    public JsonElement serialize(DesktopGameVersion src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src != null ? src.getId() : DesktopGameVersion.UNKNOWN.getId());
    }

    @Override
    public DesktopGameVersion deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            int id = json.getAsInt();
            return DesktopGameVersion.fromId(id);
        } catch (Exception e) {
            return DesktopGameVersion.UNKNOWN;
        }
    }
}

