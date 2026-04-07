package com.mass.sdk.models.account;

public final class AccountInfoDto {
    private AccountPlatform platform = AccountPlatform.DESKTOP;
    private AccountType type = AccountType.COOKIES;
    private String account = "";
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
