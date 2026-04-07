package com.mass.sdk.desktop;

import com.google.gson.reflect.TypeToken;
import com.mass.sdk.MassClient;
import com.mass.sdk.desktop.models.DesktopSession;
import com.mass.sdk.models.account.SessionDto;

import java.io.IOException;

public final class DesktopClient {
    private final MassClient client;

    public DesktopClient(MassClient client) {
        this.client = client;
    }

    public DesktopSession loginCookies(String cookies) throws IOException {
        return session(client.post("/api/desktop/login/cookies",
                MassClient.Parameters.create().add("cookies", cookies),
                TypeToken.get(SessionDto.class)));
    }

    public DesktopSession login163(String email, String password) throws IOException {
        return session(client.post("/api/desktop/login/163",
                MassClient.Parameters.create()
                        .add("email", email)
                        .add("password", password),
                TypeToken.get(SessionDto.class)));
    }

    public DesktopSession loginMobile(String mobile, String password) throws IOException {
        return session(client.post("/api/desktop/login/mobile",
                MassClient.Parameters.create()
                        .add("mobile", mobile)
                        .add("password", password),
                TypeToken.get(SessionDto.class)));
    }

    public void sendSms(String mobile) throws IOException {
        client.post("/api/desktop/login/mobile/send",
                MassClient.Parameters.create().add("mobile", mobile));
    }

    public DesktopSession loginSms(String mobile, String code) throws IOException {
        return session(client.post("/api/desktop/login/mobile/verify",
                MassClient.Parameters.create()
                        .add("mobile", mobile)
                        .add("code", code),
                TypeToken.get(SessionDto.class)));
    }

    public DesktopSession login4399Pc(String username, String password) throws IOException {
        return session(client.post("/api/desktop/login/4399pc",
                MassClient.Parameters.create()
                        .add("username", username)
                        .add("password", password),
                TypeToken.get(SessionDto.class)));
    }

    public DesktopSession login4399Com(String username, String password) throws IOException {
        return session(client.post("/api/desktop/login/4399com",
                MassClient.Parameters.create()
                        .add("username", username)
                        .add("password", password),
                TypeToken.get(SessionDto.class)));
    }

    public DesktopSession login4399ComRandom() throws IOException {
        return session(client.post("/api/desktop/login/random-4399com", TypeToken.get(SessionDto.class)));
    }

    private DesktopSession session(SessionDto session) {
        return new DesktopSession(client, session);
    }
}
