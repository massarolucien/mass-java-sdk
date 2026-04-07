package com.mass.sdk.desktop.enums;

public enum DesktopVisibilityStatus {
    PUBLIC(0),
    FRIEND(1),
    PRIVATE(2),
    PASSWORD(3);

    private final int value;

    DesktopVisibilityStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static DesktopVisibilityStatus fromValue(int value) {
        for (var status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        return PUBLIC;
    }
}
