package com.mass.sdk.mobile.models;

import com.google.gson.annotations.SerializedName;
import com.mass.sdk.mobile.interfaces.IMobileGame;

public final class MobileNetGame implements IMobileGame {
    @SerializedName("entity_id")
    private String id = "";
    @SerializedName("res_name")
    private String name = "";
    @SerializedName("brief")
    private String description = "";
    @SerializedName("online_num")
    private int playerCount;
    @SerializedName("title_image_url")
    private String imageUrl = "";

    @Override
    public String getId() {
        return id;
    }

    public MobileNetGame withId(String id) {
        this.id = id == null ? "" : id;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public MobileNetGame withName(String name) {
        this.name = name == null ? "" : name;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public MobileNetGame withDescription(String description) {
        this.description = description == null ? "" : description;
        return this;
    }

    @Override
    public int getPlayerCount() {
        return playerCount;
    }

    public MobileNetGame withPlayerCount(int playerCount) {
        this.playerCount = playerCount;
        return this;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    public MobileNetGame withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? "" : imageUrl;
        return this;
    }
}
