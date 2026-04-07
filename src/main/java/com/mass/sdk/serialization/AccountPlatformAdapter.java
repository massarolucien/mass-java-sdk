package com.mass.sdk.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mass.sdk.models.account.AccountPlatform;

import java.lang.reflect.Type;

public final class AccountPlatformAdapter implements JsonSerializer<AccountPlatform>, JsonDeserializer<AccountPlatform> {
    @Override
    public JsonElement serialize(AccountPlatform source, Type type, JsonSerializationContext context) {
        return source == null ? JsonNull.INSTANCE : new JsonPrimitive(source.value());
    }

    @Override
    public AccountPlatform deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return AccountPlatform.DESKTOP;
        }

        try {
            return AccountPlatform.fromValue(json.getAsInt());
        } catch (RuntimeException exception) {
            return AccountPlatform.DESKTOP;
        }
    }
}
