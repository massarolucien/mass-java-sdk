package com.mass.sdk.desktop.models;

import com.google.gson.annotations.SerializedName;
import com.mass.sdk.desktop.enums.DesktopGameVersion;
import com.mass.sdk.desktop.interfaces.IDesktopGame;

public class DesktopNetGame implements IDesktopGame {
    @SerializedName("entity_id")
    private String id = "";

    @SerializedName("name")
    private String name = "";

    @SerializedName("online_count")
    private long playerCount;

    @SerializedName("like_num")
    private long likeCount;

    @SerializedName("title_image_url")
    private String imageUrl = "";

    @SerializedName("brief_summary")
    private String summary = "";

    @SerializedName("download_num")
    private int downloadCount;

    @SerializedName("mc_version_id")
    private DesktopGameVersion gameVersionId = DesktopGameVersion.UNKNOWN;

    public DesktopNetGame() {}

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public DesktopGameVersion getGameVersionId() {
        return gameVersionId;
    }

    public void setGameVersionId(DesktopGameVersion gameVersionId) {
        this.gameVersionId = gameVersionId;
    }

    @Override
    public String toString() {
        return "DesktopNetGame{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", playerCount=" + playerCount +
                ", likeCount=" + likeCount +
                ", imageUrl='" + imageUrl + '\'' +
                ", summary='" + summary + '\'' +
                ", downloadCount=" + downloadCount +
                ", gameVersionId=" + gameVersionId +
                '}';
    }
}