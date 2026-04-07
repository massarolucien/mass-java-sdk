package com.mass.sdk.example;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class Server {
    private static final String DEVELOPMENT_USERNAME = "USERNAME";
    private static final String DEVELOPMENT_PASSWORD = "PASSWORD";

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .proxy(ProxySelector.of(new InetSocketAddress("p6htgxfn.cnmnmsl.top", 80)))
            .build();

    private static final Gson GSON = new Gson();

    private Server() {
    }

    public static String getToken(String username) throws IOException {
        var requestBody = GSON.toJson(new TokenRequest(DEVELOPMENT_USERNAME, DEVELOPMENT_PASSWORD, username));
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://yz.chsi.com.cn/api/development/get-token"))
                .header("Content-Type", "application/json; charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IOException("HTTP request failed: " + response.statusCode());
            }

            var body = response.body() == null ? "" : response.body().trim();
            if (body.startsWith("{")) {
                var type = new TypeToken<MassResponse<String>>() {}.getType();
                MassResponse<String> massResponse = GSON.fromJson(body, type);
                if (massResponse == null || massResponse.getCode() != 0) {
                    throw new IOException(massResponse == null ? "Empty response." : massResponse.getMessage());
                }
                return massResponse.getData();
            }

            try {
                return GSON.fromJson(body, String.class);
            } catch (Exception ignored) {
                return body;
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IOException("HTTP request interrupted.", exception);
        }
    }

    private static final class TokenRequest {
        @SerializedName("development_username")
        private final String developmentUsername;
        @SerializedName("development_password")
        private final String developmentPassword;
        private final String username;

        private TokenRequest(String developmentUsername, String developmentPassword, String username) {
            this.developmentUsername = developmentUsername;
            this.developmentPassword = developmentPassword;
            this.username = username;
        }
    }

    private static final class MassResponse<T> {
        private int code;
        @SerializedName("msg")
        private String message = "";
        private T data;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public T getData() {
            return data;
        }
    }
}
