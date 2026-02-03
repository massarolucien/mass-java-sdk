package com.mass.sdk.desktop.models;

import com.google.gson.annotations.SerializedName;
import com.mass.sdk.desktop.enums.DesktopGameVersion;
import com.mass.sdk.desktop.interfaces.IDesktopGame;

public class DesktopRentalGame implements IDesktopGame {
    @SerializedName("entity_id")
    private String id = "";

    @SerializedName("name")
    private String name = "";

    @SerializedName("player_count")
    private long playerCount;

    @SerializedName("like_num")
    private long likeCount;

    @SerializedName("image_url")
    private String imageUrl = "";

    @SerializedName("server_name")
    private String serverName = "";
    @SerializedName("has_pwd")
    private boolean hasPassword;

    @SerializedName("mc_version_id")
    private DesktopGameVersion gameVersionId = DesktopGameVersion.UNKNOWN;

    public DesktopRentalGame() {}

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getServerName() {return serverName;}

    public void setServerName(String serverName) {this.serverName = serverName;}

    @Override
    public long getPlayerCount() {
        return playerCount;
    }

    @Override
    public void setPlayerCount(long playerCount) {
        this.playerCount = playerCount;
    }

    @Override
    public long getLikeCount() {
        return likeCount;
    }

    @Override
    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public DesktopGameVersion getGameVersionId() {
        return gameVersionId;
    }

    public void setGameVersionId(DesktopGameVersion gameVersionId) {
        this.gameVersionId = gameVersionId;
    }

    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    @Override
    public String toString() {
        return "DesktopRentalGame{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", playerCount=" + playerCount +
                ", likeCount=" + likeCount +
                ", imageUrl='" + imageUrl + '\'' +
                ", gameVersionId=" + gameVersionId +
                ", hasPassword=" + hasPassword +
                '}';
    }
}