package com.mass.sdk.models;

public final class GameInstance extends MassInstance {
    private Integer processId;

    public Integer getProcessId() {
        return processId;
    }

    public GameInstance withProcessId(Integer processId) {
        this.processId = processId;
        return this;
    }
}
