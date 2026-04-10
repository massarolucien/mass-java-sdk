package com.mass.sdk.models.account;

import com.google.gson.annotations.SerializedName;

public final class SessionDto {
    @SerializedName("user_id")
    private String userId = "";
    @SerializedName("cookies")
    private String cookies = "";
    @SerializedName("nickname")
    private String nickname = "";
    @SerializedName("info")
    private AccountInfoDto info = new AccountInfoDto();

    public String getUserId() {
        return userId;
    }

    public SessionDto withUserId(String userId) {
        this.userId = userId == null ? "" : userId;
        return this;
    }

    public String getCookies() {
        return cookies;
    }

    public SessionDto withCookies(String cookies) {
        this.cookies = cookies == null ? "" : cookies;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public SessionDto withNickname(String nickname) {
        this.nickname = nickname == null ? "" : nickname;
        return this;
    }

    public AccountInfoDto getInfo() {
        return info;
    }

    public SessionDto withInfo(AccountInfoDto info) {
        this.info = info == null ? new AccountInfoDto() : info;
        return this;
    }
}
