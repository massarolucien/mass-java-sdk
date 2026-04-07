package com.mass.sdk.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mass.sdk.models.account.AccountType;

import java.lang.reflect.Type;

public final class AccountTypeAdapter implements JsonSerializer<AccountType>, JsonDeserializer<AccountType> {
    @Override
    public JsonElement serialize(AccountType source, Type type, JsonSerializationContext context) {
        return source == null ? JsonNull.INSTANCE : new JsonPrimitive(source.value());
    }

    @Override
    public AccountType deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return AccountType.COOKIES;
        }

        try {
            return AccountType.fromValue(json.getAsInt());
        } catch (RuntimeException exception) {
            return AccountType.COOKIES;
        }
    }
}
