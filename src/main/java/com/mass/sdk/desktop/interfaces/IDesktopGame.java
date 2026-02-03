package com.mass.sdk.desktop.interfaces;

public interface IDesktopGame {
    String getId();
    void setId(String id);

    String getName();
    void setName(String name);

    long getPlayerCount();
    void setPlayerCount(long playerCount);

    long getLikeCount();
    void setLikeCount(long likeCount);

    String getImageUrl();
    void setImageUrl(String imageUrl);
}