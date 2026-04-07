package com.mass.sdk.models.account;

public enum AccountPlatform {
    DESKTOP(0),
    MOBILE(1);

    private final int value;

    AccountPlatform(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static AccountPlatform fromValue(int value) {
        for (var platform : values()) {
            if (platform.value == value) {
                return platform;
            }
        }
        return DESKTOP;
    }
}
