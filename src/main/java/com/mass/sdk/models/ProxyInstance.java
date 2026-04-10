package com.mass.sdk.models;

import com.google.gson.annotations.SerializedName;

public final class ProxyInstance extends MassInstance {
    @SerializedName("port")
    private int port;

    public int getPort() {
        return port;
    }

    public ProxyInstance withPort(int port) {
        this.port = port;
        return this;
    }
}
