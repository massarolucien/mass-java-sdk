package com.mass.sdk.desktop.models;

import com.google.gson.annotations.SerializedName;
import com.mass.sdk.desktop.interfaces.IDesktopGameCharacter;

import java.time.Instant;

public final class DesktopNetGameCharacter implements IDesktopGameCharacter {
    @SerializedName("game_id")
    private String gameId = "";
    @SerializedName("name")
    private String name = "";
    @SerializedName("create_time")
    private Instant createTime = Instant.EPOCH;

    @Override
    public String getGameId() {
        return gameId;
    }

    public DesktopNetGameCharacter withGameId(String gameId) {
        this.gameId = gameId == null ? "" : gameId;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public DesktopNetGameCharacter withName(String name) {
        this.name = name == null ? "" : name;
        return this;
    }

    @Override
    public Instant getCreateTime() {
        return createTime;
    }

    public DesktopNetGameCharacter withCreateTime(Instant createTime) {
        this.createTime = createTime == null ? Instant.EPOCH : createTime;
        return this;
    }
}
