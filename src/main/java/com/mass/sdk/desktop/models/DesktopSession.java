package com.mass.sdk.desktop.models;

import com.google.gson.reflect.TypeToken;
import com.mass.sdk.MassClient;
import com.mass.sdk.Page;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class DesktopSession {
    private final MassClient client;
    private final String userId;

    public DesktopSession(MassClient client, String userId) {
        this.client = client;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public List<DesktopNetGame> getNetGames() throws IOException {
        return client.request("GET", "/api/desktop/" + userId + "/net-game/list", null,
            new TypeToken<>() {});
    }

    public List<DesktopRentalGame> getRentalGames() throws IOException {
        return client.request("GET", "/api/desktop/" + userId + "/rental-game/list", null,
            new TypeToken<>() {});
    }

    public Page<DesktopSkin> getSkins(int page) throws IOException {
        PageRequest request = new PageRequest(page);
        return client.request("GET", "/api/desktop/" + userId + "/skin/list", request,
            new TypeToken<>() {});
    }

    public Page<DesktopSkin> getOwnedSkins(int page) throws IOException {
        PageRequest request = new PageRequest(page);
        return client.request("GET", "/api/desktop/" + userId + "/skin/owned-list", request,
            new TypeToken<>() {});
    }

    public List<DesktopNetGameCharacter> getNetGameCharacters(String gameId) throws IOException {
        return client.request("GET", "/api/desktop/" + userId + "/net-game/" + gameId + "/list", null,
            new TypeToken<>() {});
    }

    public List<DesktopRentalGameCharacter> getRentalGameCharacters(String gameId) throws IOException {
        return client.request("GET", "/api/desktop/" + userId + "/rental-game/" + gameId + "/list", null,
            new TypeToken<>() {});
    }

    public void addNetGameCharacter(String gameId, String name) throws IOException {
        AddCharacterRequest request = new AddCharacterRequest(name);
        client.request("POST", "/api/desktop/" + userId + "/net-game/" + gameId + "/add", request,
            new TypeToken<Void>() {});
    }

    public void addRentalGameCharacter(String gameId, String name) throws IOException {
        AddCharacterRequest request = new AddCharacterRequest(name);
        client.request("POST", "/api/desktop/" + userId + "/rental-game/" + gameId + "/add", request,
            new TypeToken<Void>() {});
    }

    public void setSkin(String itemId) throws IOException {
        client.request("POST", "/api/desktop/" + userId + "/skin/" + itemId + "/set", new Object(),
            new TypeToken<Void>() {});
    }

    public int startNetGameProxy(String gameId, String name) throws IOException {
        return client.request("POST", "/api/desktop/" + userId + "/net-game/" + gameId + "/" + name + "/start-proxy", null,
            new TypeToken<>() {});
    }

    public int startRentalGameProxy(String gameId, String name, String password) throws IOException {
        StartProxyRequest request = new StartProxyRequest(password);
        return client.request("POST", "/api/desktop/" + userId + "/rental-game/" + gameId + "/" + name + "/start-proxy", request,
            new TypeToken<>() {});
    }

    public int startRentalGameProxy(String gameId, String name) throws IOException {
        return this.startRentalGameProxy(gameId, name, null);
    }

    private static class AddCharacterRequest {
        private final String name;

        public AddCharacterRequest(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static class StartProxyRequest {
        private final String password;

        public StartProxyRequest(String password) {
            this.password = password;
        }

        public String getPassword() {
            return password;
        }
    }

    private static class PageRequest {
        private final int page;

        public PageRequest(int page) {
            this.page = page;
        }

        public int getPage() {
            return page;
        }
    }
}