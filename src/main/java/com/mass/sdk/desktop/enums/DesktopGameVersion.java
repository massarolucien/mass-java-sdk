package com.mass.sdk.desktop.enums;

public enum DesktopGameVersion {
    NONE(0),
    CPP(100000000L),
    RTX(200000000L),
    V1710(1007010L),
    V1102(1010002L),
    V18(1008000L),
    V1112(1011002L),
    V1122(1012002L),
    V188(1008008L),
    V189(1008009L),
    V194(1009004L),
    V164(1006004L),
    V172(1007002L),
    V112(1012000L),
    V1132(1013002L),
    V1143(1014003L),
    V115(1015000L),
    V116(1016000L),
    V118(1018000L),
    V1192(1019002L),
    V120(1020000L),
    V1206(1020006L),
    V121(1021000L);

    private final long value;

    DesktopGameVersion(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }

    public static DesktopGameVersion fromValue(long value) {
        for (var version : values()) {
            if (version.value == value) {
                return version;
            }
        }
        return NONE;
    }
}
