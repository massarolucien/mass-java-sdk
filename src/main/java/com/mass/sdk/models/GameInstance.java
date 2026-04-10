package com.mass.sdk.models;

import com.google.gson.annotations.SerializedName;

public final class GameInstance extends MassInstance {
    @SerializedName("process_id")
    private Integer processId;

    public Integer getProcessId() {
        return processId;
    }

    public GameInstance withProcessId(Integer processId) {
        this.processId = processId;
        return this;
    }
}
