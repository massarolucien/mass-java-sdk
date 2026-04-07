package com.mass.sdk.desktop.enums;

public enum DesktopServerType {
    DOCKER("docker"),
    VMWARE("vmware");

    private final String value;

    DesktopServerType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static DesktopServerType fromValue(String value) {
        for (var type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return DOCKER;
    }
}
