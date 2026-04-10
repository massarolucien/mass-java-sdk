package com.mass.sdk.models;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;

public class MassInstance {
    @SerializedName("user_id")
    private String userId = "";
    @SerializedName("type")
    private String type = "";
    @SerializedName("id")
    private long id;
    @SerializedName("launch_time")
    private Instant launchTime = Instant.EPOCH;

    public String getUserId() {
        return userId;
    }

    public MassInstance withUserId(String userId) {
        this.userId = userId == null ? "" : userId;
        return this;
    }

    public String getType() {
        return type;
    }

    public MassInstance withType(String type) {
        this.type = type == null ? "" : type;
        return this;
    }

    public long getId() {
        return id;
    }

    public MassInstance withId(long id) {
        this.id = id;
        return this;
    }

    public Instant getLaunchTime() {
        return launchTime;
    }

    public MassInstance withLaunchTime(Instant launchTime) {
        this.launchTime = launchTime == null ? Instant.EPOCH : launchTime;
        return this;
    }
}
