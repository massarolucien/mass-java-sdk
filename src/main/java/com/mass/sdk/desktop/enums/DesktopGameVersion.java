package com.mass.sdk.desktop.enums;

public enum DesktopGameVersion {

    UNKNOWN(0),
    V1_7_10(1710),
    V1_8_9(189),
    V1_12_2(1122),
    V1_16_5(1165),
    V1_17_1(1171),
    V1_18_2(1182),
    V1_19_2(1192),
    V1_19_3(1193),
    V1_19_4(1194),
    V1_20_1(1201),
    V1_20_2(1202),
    V1_20_4(1204),
    V1_21(1210);

    private final int id;

    DesktopGameVersion(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static DesktopGameVersion fromId(int id) {
        for (DesktopGameVersion version : values()) {
            if (version.id == id) {
                return version;
            }
        }
        return UNKNOWN;
    }
}