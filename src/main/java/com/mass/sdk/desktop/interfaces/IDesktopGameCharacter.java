package com.mass.sdk.desktop.interfaces;

import java.time.Instant;

public interface IDesktopGameCharacter {
    String getGameId();

    String getName();

    Instant getCreateTime();
}
