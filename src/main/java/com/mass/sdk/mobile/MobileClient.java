package com.mass.sdk.mobile;

import com.google.gson.reflect.TypeToken;
import com.mass.sdk.MassClient;
import com.mass.sdk.mobile.models.MobileSession;
import com.mass.sdk.models.account.SessionDto;

import java.io.IOException;

public final class MobileClient {
    private final MassClient client;

    public MobileClient(MassClient client) {
        this.client = client;
    }

    public MobileSession loginCookies(String cookies) throws IOException {
        return session(client.post("/api/mobile/login/cookies",
                MassClient.Parameters.create().add("cookies", cookies),
                TypeToken.get(SessionDto.class)));
    }

    public MobileSession login163(String email, String password) throws IOException {
        return session(client.post("/api/mobile/login/163",
                MassClient.Parameters.create()
                        .add("email", email)
                        .add("password", password),
                TypeToken.get(SessionDto.class)));
    }

    public MobileSession loginMobile(String mobile, String password) throws IOException {
        return session(client.post("/api/mobile/login/mobile",
                MassClient.Parameters.create()
                        .add("mobile", mobile)
                        .add("password", password),
                TypeToken.get(SessionDto.class)));
    }

    public void sendSms(String mobile) throws IOException {
        client.post("/api/mobile/login/mobile/send",
                MassClient.Parameters.create().add("mobile", mobile));
    }

    public MobileSession loginSms(String mobile, String code) throws IOException {
        return session(client.post("/api/mobile/login/mobile/verify",
                MassClient.Parameters.create()
                        .add("mobile", mobile)
                        .add("code", code),
                TypeToken.get(SessionDto.class)));
    }

    public MobileSession login4399Com(String username, String password) throws IOException {
        return session(client.post("/api/mobile/login/4399com",
                MassClient.Parameters.create()
                        .add("username", username)
                        .add("password", password),
                TypeToken.get(SessionDto.class)));
    }

    public MobileSession login4399ComRandom() throws IOException {
        return session(client.post("/api/mobile/login/random-4399com", TypeToken.get(SessionDto.class)));
    }

    private MobileSession session(SessionDto session) {
        return new MobileSession(client, session);
    }
}
