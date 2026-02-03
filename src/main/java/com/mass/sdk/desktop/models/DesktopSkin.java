package com.mass.sdk.desktop.models;

import com.google.gson.annotations.SerializedName;

public class DesktopSkin {
    @SerializedName("entity_id")
    private String id = "";

    @SerializedName("name")
    private String name = "";

    @SerializedName("brief_summary")
    private String briefSummary = "";

    @SerializedName("image_url")
    private String imageUrl = "";

    public DesktopSkin() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBriefSummary() {
        return briefSummary;
    }

    public void setBriefSummary(String briefSummary) {
        this.briefSummary = briefSummary;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "DesktopSkin{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", briefSummary='" + briefSummary + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}