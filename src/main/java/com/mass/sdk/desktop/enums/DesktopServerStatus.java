package com.mass.sdk.desktop.enums;

public enum DesktopServerStatus {
    NONE(-1),
    SERVER_OFF(0),
    SERVER_ON(1),
    UNINITIALIZED(2),
    OPENING(3),
    CLOSING(4),
    OUT_OF_DATE(5),
    SAVE_CLEANING(6),
    RESETTING(7),
    UPGRADING(8),
    DISC_OVERFLOW(9);

    private final int value;

    DesktopServerStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static DesktopServerStatus fromValue(int value) {
        for (var status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        return NONE;
    }
}
