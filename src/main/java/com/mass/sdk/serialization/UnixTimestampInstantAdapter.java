package com.mass.sdk.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.Instant;

public final class UnixTimestampInstantAdapter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {
    @Override
    public JsonElement serialize(Instant source, Type type, JsonSerializationContext context) {
        return source == null ? JsonNull.INSTANCE : new JsonPrimitive(source.getEpochSecond());
    }

    @Override
    public Instant deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return Instant.EPOCH;
        }

        if (json.isJsonPrimitive()) {
            var primitive = json.getAsJsonPrimitive();

            if (primitive.isNumber()) {
                return Instant.ofEpochSecond(primitive.getAsLong());
            }

            if (primitive.isString()) {
                var value = primitive.getAsString();
                try {
                    return Instant.parse(value);
                } catch (Exception ignored) {
                    try {
                        return Instant.ofEpochSecond(Long.parseLong(value));
                    } catch (NumberFormatException exception) {
                        throw new JsonParseException("Unsupported instant value: " + value, exception);
                    }
                }
            }
        }

        throw new JsonParseException("Unsupported instant payload: " + json);
    }
}
