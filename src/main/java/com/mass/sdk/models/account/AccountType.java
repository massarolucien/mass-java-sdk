package com.mass.sdk.models.account;

public enum AccountType {
    COOKIES(0),
    PC_4399(1),
    EMAIL_163(2),
    MOBILE_PWD(3),
    MOBILE_SMS(4),
    RANDOM_COM_4399(5),
    COM_4399(6);

    private final int value;

    AccountType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static AccountType fromValue(int value) {
        for (var type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        return COOKIES;
    }
}
