package com.mass.sdk.models.account;

import com.google.gson.annotations.SerializedName;

public final class AccountInfoDto {
    @SerializedName("platform")
    private AccountPlatform platform = AccountPlatform.DESKTOP;
    @SerializedName("type")
    private AccountType type = AccountType.COOKIES;
    @SerializedName("account")
    private String account = "";
    @SerializedName("password")
    private String password = "";

    public AccountPlatform getPlatform() {
        return platform;
    }

    public AccountInfoDto withPlatform(AccountPlatform platform) {
        this.platform = platform == null ? AccountPlatform.DESKTOP : platform;
        return this;
    }

    public AccountType getType() {
        return type;
    }

    public AccountInfoDto withType(AccountType type) {
        this.type = type == null ? AccountType.COOKIES : type;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public AccountInfoDto withAccount(String account) {
        this.account = account == null ? "" : account;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AccountInfoDto withPassword(String password) {
        this.password = password == null ? "" : password;
        return this;
    }
}
