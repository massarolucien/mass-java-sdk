package com.mass.sdk.desktop.models;

import com.google.gson.annotations.SerializedName;
import com.mass.sdk.desktop.interfaces.IDesktopGameCharacter;

public class DesktopNetGameCharacter implements IDesktopGameCharacter {
    @SerializedName("name")
    private String name = "";

    @SerializedName("game_id")
    private String gameId = "";

    @SerializedName("create_time")
    private long createTime;

    public DesktopNetGameCharacter() {}

    public DesktopNetGameCharacter(String name, String gameId) {
        this.name = name;
        this.gameId = gameId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getGameId() {
        return gameId;
    }

    @Override
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    @Override
    public long getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "DesktopNetGameCharacter{" +
                ", name='" + name + '\'' +
                ", gameId='" + gameId + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}