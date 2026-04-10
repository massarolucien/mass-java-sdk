package com.mass.sdk.desktop.models;

import com.google.gson.annotations.SerializedName;

public final class DesktopSkin {
    @SerializedName("entity_id")
    private String id = "";
    @SerializedName("name")
    private String name = "";
    @SerializedName("brief_summary")
    private String briefSummary = "";
    @SerializedName("image_url")
    private String imageUrl = "";

    public String getId() {
        return id;
    }

    public DesktopSkin withId(String id) {
        this.id = id == null ? "" : id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DesktopSkin withName(String name) {
        this.name = name == null ? "" : name;
        return this;
    }

    public String getBriefSummary() {
        return briefSummary;
    }

    public DesktopSkin withBriefSummary(String briefSummary) {
        this.briefSummary = briefSummary == null ? "" : briefSummary;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public DesktopSkin withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? "" : imageUrl;
        return this;
    }
}
