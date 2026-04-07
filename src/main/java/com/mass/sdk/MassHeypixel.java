package com.mass.sdk;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public final class MassHeypixel {
    private MassHeypixel() {
    }

    public static String StartProxy(int startPort, String token) throws IOException, InterruptedException {
        var httpClient = HttpClient.newBuilder().build();
        var port = startPort;
        var connected = false;

        do {
            try {
                var response = httpClient.send(
                        HttpRequest.newBuilder()
                                .uri(URI.create("http://127.0.0.1:" + port + "/api/base/ping"))
                                .GET()
                                .build(),
                        HttpResponse.BodyHandlers.ofString());

                if (response.body().contains("pang")) {
                    connected = true;
                    break;
                }
            } catch (Exception ignored) {
            }

            port++;
        } while (port < startPort + 10);

        if (!connected) {
            throw new IOException("Mass local service was not found.");
        }

        var response = httpClient.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:" + port + "/api/heypixel/start-proxy"))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(HttpRequest.BodyPublishers.ofString("token=" + URLEncoder.encode(token, StandardCharsets.UTF_8)))
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("HTTP request failed: " + response.statusCode() + " " + response.body());
        }

        var responseBody = response.body();
        try {
            Integer.parseInt(responseBody);
            return "127.0.0.1:" + responseBody;
        } catch (NumberFormatException exception) {
            throw new IOException(responseBody, exception);
        }
    }
}
