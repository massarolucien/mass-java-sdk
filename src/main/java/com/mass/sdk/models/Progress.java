package com.mass.sdk.models;

import com.google.gson.annotations.SerializedName;

public final class Progress {
    @SerializedName("step")
    private int step;
    @SerializedName("total")
    private int total;
    @SerializedName("percentage")
    private int percentage;
    @SerializedName("message")
    private String message = "";

    public int getStep() {
        return step;
    }

    public Progress withStep(int step) {
        this.step = step;
        return this;
    }

    public int getTotal() {
        return total;
    }

    public Progress withTotal(int total) {
        this.total = total;
        return this;
    }

    public int getPercentage() {
        return percentage;
    }

    public Progress withPercentage(int percentage) {
        this.percentage = percentage;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Progress withMessage(String message) {
        this.message = message == null ? "" : message;
        return this;
    }
}
