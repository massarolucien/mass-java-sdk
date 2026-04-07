package com.mass.sdk.desktop.models;

import com.google.gson.reflect.TypeToken;
import com.mass.sdk.MassClient;
import com.mass.sdk.models.GameInstance;
import com.mass.sdk.models.Page;
import com.mass.sdk.models.Progress;
import com.mass.sdk.models.ProxyInstance;
import com.mass.sdk.models.account.AccountInfoDto;
import com.mass.sdk.models.account.SessionDto;

import java.io.IOException;
import java.util.List;

public class DesktopSession {
    protected final MassClient client;
    private final String userId;
    private final String cookies;
    private final String nickname;
    private final AccountInfoDto info;

    public DesktopSession(MassClient client, SessionDto session) {
        this.client = client;
        this.userId = session == null ? "" : session.getUserId();
        this.cookies = session == null ? "" : session.getCookies();
        this.nickname = session == null ? "" : session.getNickname();
        this.info = session == null || session.getInfo() == null ? new AccountInfoDto() : session.getInfo();
    }

    public DesktopSession(MassClient client, String userId) {
        this(client, new SessionDto().withUserId(userId));
    }

    public String getUserId() {
        return userId;
    }

    public String getCookies() {
        return cookies;
    }

    public String getNickname() {
        return nickname;
    }

    public AccountInfoDto getInfo() {
        return info;
    }

    protected String desktopRoute(String suffix) {
        return "/api/desktop/" + userId + suffix;
    }

    public List<DesktopNetGame> getDesktopNetGames() throws IOException {
        return client.get(desktopRoute("/net-game/list"), new TypeToken<>() {});
    }

    public List<DesktopRentalGame> getDesktopRentalGames() throws IOException {
        return client.get(desktopRoute("/rental-game/list"), new TypeToken<>() {});
    }

    public Page<DesktopSkin> getDesktopSkins(int page) throws IOException {
        return client.get(desktopRoute("/skin/list"),
                MassClient.Parameters.create().add("page", page),
                new TypeToken<>() {});
    }

    public Page<DesktopSkin> getDesktopOwnedSkins(int page) throws IOException {
        return client.get(desktopRoute("/skin/owned-list"),
                MassClient.Parameters.create().add("page", page),
                new TypeToken<>() {});
    }

    public List<DesktopNetGameCharacter> getDesktopNetGameCharacters(String gameId) throws IOException {
        return client.get(desktopRoute("/net-game/" + gameId + "/list"), new TypeToken<>() {});
    }

    public List<DesktopRentalGameCharacter> getDesktopRentalGameCharacters(String gameId) throws IOException {
        return client.get(desktopRoute("/rental-game/" + gameId + "/list"), new TypeToken<>() {});
    }

    public void addDesktopNetGameCharacter(String gameId, String name) throws IOException {
        client.post(desktopRoute("/net-game/" + gameId + "/add"),
                MassClient.Parameters.create().add("name", name));
    }

    public void addDesktopRentalGameCharacter(String gameId, String name) throws IOException {
        client.post(desktopRoute("/rental-game/" + gameId + "/add"),
                MassClient.Parameters.create().add("name", name));
    }

    public void setDesktopSkin(String itemId) throws IOException {
        client.post(desktopRoute("/skin/" + itemId + "/set"), MassClient.Parameters.empty());
    }

    public ProxyInstance startDesktopNetGameProxy(String gameId, String name) throws IOException {
        return client.post(desktopRoute("/net-game/" + gameId + "/" + name + "/start-proxy"),
                TypeToken.get(ProxyInstance.class));
    }

    public ProxyInstance startDesktopRentalGameProxy(String gameId, String name) throws IOException {
        return startDesktopRentalGameProxy(gameId, name, null);
    }

    public ProxyInstance startDesktopRentalGameProxy(String gameId, String name, String password) throws IOException {
        return client.post(desktopRoute("/rental-game/" + gameId + "/" + name + "/start-proxy"),
                MassClient.Parameters.create().add("password", password),
                TypeToken.get(ProxyInstance.class));
    }

    public GameInstance startDesktopNetJavaGame(String gameId, String name) throws IOException {
        return client.progress(desktopRoute("/net-game/" + gameId + "/" + name + "/start-game"));
    }

    public GameInstance startDesktopNetJavaGame(String gameId, String name, java.util.function.Consumer<Progress> progressConsumer) throws IOException {
        return client.progress(desktopRoute("/net-game/" + gameId + "/" + name + "/start-game"), progressConsumer);
    }

    public GameInstance startDesktopRentalJavaGame(String gameId, String name) throws IOException {
        return startDesktopRentalJavaGame(gameId, name, null, null);
    }

    public GameInstance startDesktopRentalJavaGame(String gameId, String name, java.util.function.Consumer<Progress> progressConsumer) throws IOException {
        return startDesktopRentalJavaGame(gameId, name, null, progressConsumer);
    }

    public GameInstance startDesktopRentalJavaGame(String gameId, String name, String password) throws IOException {
        return startDesktopRentalJavaGame(gameId, name, password, null);
    }

    public GameInstance startDesktopRentalJavaGame(String gameId, String name, String password, java.util.function.Consumer<Progress> progressConsumer) throws IOException {
        var suffix = "/rental-game/" + gameId + "/" + name + "/start-game";
        var path = password == null || password.isEmpty()
                ? desktopRoute(suffix)
                : desktopRoute(suffix) + "?password=" + java.net.URLEncoder.encode(password, java.nio.charset.StandardCharsets.UTF_8);
        return client.progress(path, progressConsumer);
    }
}
