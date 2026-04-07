package com.mass.sdk.mobile.models;

import com.google.gson.reflect.TypeToken;
import com.mass.sdk.MassClient;
import com.mass.sdk.desktop.models.DesktopSession;
import com.mass.sdk.models.GameInstance;
import com.mass.sdk.models.Progress;
import com.mass.sdk.models.account.SessionDto;

import java.io.IOException;
import java.util.List;

public final class MobileSession extends DesktopSession {
    public MobileSession(MassClient client, SessionDto session) {
        super(client, session);
    }

    public MobileSession(MassClient client, String userId) {
        super(client, userId);
    }

    private String mobileRoute(String suffix) {
        return "/api/mobile/" + getUserId() + suffix;
    }

    public List<MobileNetGame> getMobileNetGames() throws IOException {
        return client.get(mobileRoute("/net-game/list"), new TypeToken<>() {});
    }

    public GameInstance startMobileNetCppGame(String gameId) throws IOException {
        return client.progress(mobileRoute("/net-game/" + gameId + "/start-game"));
    }

    public GameInstance startMobileNetCppGame(String gameId, java.util.function.Consumer<Progress> progressConsumer) throws IOException {
        return client.progress(mobileRoute("/net-game/" + gameId + "/start-game"), progressConsumer);
    }
}
