package com.mass.sdk.desktop.interfaces;

public interface IDesktopGameCharacter {
    String getName();
    void setName(String name);

    String getGameId();
    void setGameId(String gameId);

    long getCreateTime();
    void setCreateTime(long createTime);
}