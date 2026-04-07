package com.mass.sdk.models;

public final class Progress {
    private int step;
    private int total;
    private int percentage;
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
