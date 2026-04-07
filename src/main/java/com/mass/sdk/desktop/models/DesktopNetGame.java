package com.mass.sdk.desktop.models;

import com.google.gson.annotations.SerializedName;
import com.mass.sdk.desktop.enums.DesktopGameVersion;
import com.mass.sdk.desktop.interfaces.IDesktopGame;

public final class DesktopNetGame implements IDesktopGame {
    @SerializedName("entity_id")
    private String id = "";
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
    private DesktopGameVersion gameVersionId = DesktopGameVersion.NONE;

    @Override
    public String getId() {
        return id;
    }

    public DesktopNetGame withId(String id) {
        this.id = id == null ? "" : id;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public DesktopNetGame withName(String name) {
        this.name = name == null ? "" : name;
        return this;
    }

    @Override
    public long getPlayerCount() {
        return playerCount;
    }

    public DesktopNetGame withPlayerCount(long playerCount) {
        this.playerCount = playerCount;
        return this;
    }

    @Override
    public long getLikeCount() {
        return likeCount;
    }

    public DesktopNetGame withLikeCount(long likeCount) {
        this.likeCount = likeCount;
        return this;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    public DesktopNetGame withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? "" : imageUrl;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public DesktopNetGame withSummary(String summary) {
        this.summary = summary == null ? "" : summary;
        return this;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public DesktopNetGame withDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
        return this;
    }

    public DesktopGameVersion getGameVersionId() {
        return gameVersionId;
    }

    public DesktopNetGame withGameVersionId(DesktopGameVersion gameVersionId) {
        this.gameVersionId = gameVersionId == null ? DesktopGameVersion.NONE : gameVersionId;
        return this;
    }
}
