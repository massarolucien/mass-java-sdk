package com.mass.sdk.desktop.models;

import com.google.gson.annotations.SerializedName;
import com.mass.sdk.desktop.interfaces.IDesktopGameCharacter;

public class DesktopRentalGameCharacter implements IDesktopGameCharacter {
    @SerializedName("name")
    private String name = "";

    @SerializedName("server_id")
    private String gameId = "";

    @SerializedName("create_ts")
    private long createTime;

    public DesktopRentalGameCharacter() {}

    public DesktopRentalGameCharacter(String name, String gameId) {
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
        return "DesktopRentalGameCharacter{" +
                ", name='" + name + '\'' +
                ", gameId='" + gameId + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}