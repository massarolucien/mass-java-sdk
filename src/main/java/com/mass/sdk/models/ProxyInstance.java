package com.mass.sdk.models;

public final class ProxyInstance extends MassInstance {
    private int port;

    public int getPort() {
        return port;
    }

    public ProxyInstance withPort(int port) {
        this.port = port;
        return this;
    }
}
