package com.mass.sdk.desktop;

import com.google.gson.reflect.TypeToken;
import com.mass.sdk.MassClient;
import com.mass.sdk.desktop.models.DesktopSession;

import java.io.IOException;

public class DesktopClient {
    private final MassClient client;

    public DesktopClient(MassClient client) {
        this.client = client;
    }

    public DesktopSession randomLogin() throws IOException {
        String userId = client.request("POST", "/api/mobile/login/random", null,
            new TypeToken<>() {});
        return new DesktopSession(client, userId);
    }

    public DesktopSession loginCookies(String cookies) throws IOException {
        LoginCookiesRequest request = new LoginCookiesRequest(cookies);
        String userId = client.request("POST", "/api/desktop/login/cookies", request,
            new TypeToken<>() {});
        return new DesktopSession(client, userId);
    }

    public DesktopSession login163(String email, String password) throws IOException {
        Login163Request request = new Login163Request(email, password);
        String userId = client.request("POST", "/api/desktop/login/163", request,
            new TypeToken<>() {});
        return new DesktopSession(client, userId);
    }

    public DesktopSession loginMobile(String mobile, String password) throws IOException {
        LoginMobileRequest request = new LoginMobileRequest(mobile, password);
        String userId = client.request("POST", "/api/desktop/login/mobile", request,
            new TypeToken<>() {});
        return new DesktopSession(client, userId);
    }

    public void sendSms(String mobile) throws IOException {
        SendSmsRequest request = new SendSmsRequest(mobile);
        client.request("POST", "/api/desktop/login/mobile/send", request,
            new TypeToken<Void>() {});
    }

    public DesktopSession loginSms(String mobile, String code) throws IOException {
        LoginSmsRequest request = new LoginSmsRequest(mobile, code);
        String userId = client.request("POST", "/api/desktop/login/mobile/verify", request,
            new TypeToken<>() {});
        return new DesktopSession(client, userId);
    }

    public DesktopSession login4399(String username, String password) throws IOException {
        Login4399Request request = new Login4399Request(username, password);
        String userId = client.request("POST", "/api/desktop/login/4399", request,
            new TypeToken<>() {});
        return new DesktopSession(client, userId);
    }

    private static class LoginCookiesRequest {
        private final String cookies;

        public LoginCookiesRequest(String cookies) {
            this.cookies = cookies;
        }

        public String getCookies() {
            return cookies;
        }
    }

    private static class Login163Request {
        private final String email;
        private final String password;

        public Login163Request(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }

    private static class LoginMobileRequest {
        private final String mobile;
        private final String password;

        public LoginMobileRequest(String mobile, String password) {
            this.mobile = mobile;
            this.password = password;
        }

        public String getMobile() {
            return mobile;
        }

        public String getPassword() {
            return password;
        }
    }

    private static class SendSmsRequest {
        private final String mobile;

        public SendSmsRequest(String mobile) {
            this.mobile = mobile;
        }

        public String getMobile() {
            return mobile;
        }
    }

    private static class LoginSmsRequest {
        private final String mobile;
        private final String code;

        public LoginSmsRequest(String mobile, String code) {
            this.mobile = mobile;
            this.code = code;
        }

        public String getMobile() {
            return mobile;
        }

        public String getCode() {
            return code;
        }
    }

    private static class Login4399Request {
        private final String username;
        private final String password;

        public Login4399Request(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}