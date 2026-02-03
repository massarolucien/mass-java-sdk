package com.mass.sdk;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mass.sdk.desktop.DesktopClient;
import com.mass.sdk.desktop.enums.DesktopGameVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class MassClient {
    private static final Logger logger = LoggerFactory.getLogger(MassClient.class);

    private final HttpClient httpClient;
    private final Gson gson;
    private final String baseUrl;

    public MassClient(String baseUrl) {
        String normalized = baseUrl;
        if (!(normalized.startsWith("http://") || normalized.startsWith("https://"))) {
            normalized = "http://" + normalized;
        }
        this.baseUrl = normalized.endsWith("/") ? normalized.substring(0, normalized.length() - 1) : normalized;

        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(DesktopGameVersion.class, new com.mass.sdk.desktop.enums.DesktopGameVersionAdapter())
                .create();

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();

        this.desktop = new DesktopClient(this);
    }

    private String camelToSnake(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    private void addQueryParameters(StringBuilder urlBuilder, Object obj) throws IOException {
        if (obj == null) return;

        try {
            Class<?> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);

                String fieldName = camelToSnake(field.getName());

                String stringValue;
                if (value == null) {
                    stringValue = "";
                } else if (value instanceof Boolean) {
                    stringValue = ((Boolean) value) ? "1" : "0";
                } else {
                    stringValue = value.toString();
                }

                if (urlBuilder.indexOf("?") < 0) {
                    urlBuilder.append("?");
                } else {
                    urlBuilder.append("&");
                }

                urlBuilder.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                        .append("=")
                        .append(URLEncoder.encode(stringValue, StandardCharsets.UTF_8));
            }
        } catch (IllegalAccessException e) {
            throw new IOException("无法转换对象为查询参数", e);
        }
    }

    private String objectToFormBody(Object obj) throws IOException {
        StringBuilder builder = new StringBuilder();

        if (obj == null) {
            return "";
        }

        try {
            Class<?> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);

                String fieldName = camelToSnake(field.getName());

                String stringValue;
                if (value == null) {
                    stringValue = "";
                } else if (value instanceof Boolean) {
                    stringValue = ((Boolean) value) ? "1" : "0";
                } else {
                    stringValue = value.toString();
                }

                if (!stringValue.isEmpty()) {
                    if (builder.length() > 0) {
                        builder.append("&");
                    }
                    builder.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                            .append("=")
                            .append(URLEncoder.encode(stringValue, StandardCharsets.UTF_8));
                }
            }
        } catch (IllegalAccessException e) {
            throw new IOException("无法转换对象为表单数据", e);
        }

        return builder.toString();
    }

    public <T> T get(String path, TypeToken<T> responseTypeToken) throws IOException {
        return request("GET", path, null, responseTypeToken);
    }

    public <T> T request(String path, Class<T> responseType) throws IOException {
        return request("POST", path, null, TypeToken.get(responseType));
    }

    public <T> T request(String method, String path, Object body, TypeToken<T> responseTypeToken) throws IOException {
        String url = baseUrl + path;
        HttpRequest.Builder requestBuilder;

        if ("GET".equalsIgnoreCase(method)) {
            StringBuilder urlBuilder = new StringBuilder(url);
            if (body != null) {
                addQueryParameters(urlBuilder, body);
            }
            requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(urlBuilder.toString()))
                    .GET();
        } else {
            String formBody = objectToFormBody(body);
            requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(formBody));
        }

        HttpRequest request = requestBuilder.build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IOException("HTTP请求失败: " + response.statusCode() + " " + response.body());
            }

            return parseApiResponse(response.body(), responseTypeToken);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("HTTP请求被中断", e);
        }
    }

    private <T> T parseApiResponse(String responseBody, TypeToken<T> responseTypeToken) throws IOException {
        Type responseType = responseTypeToken.getType();

        if (responseType == Void.class) {
            Type apiResponseType = TypeToken.getParameterized(ApiResponse.class, Object.class).getType();
            ApiResponse<?> apiResponse = gson.fromJson(responseBody, apiResponseType);
            if (apiResponse.getCode() != 200) {
                throw new IOException("API请求失败: " + apiResponse.getMsg());
            }
            return null;
        }

        Type apiResponseType = TypeToken.getParameterized(ApiResponse.class, responseType).getType();

        ApiResponse<T> apiResponse = gson.fromJson(responseBody, apiResponseType);

        if (apiResponse.getCode() != 200) {
            throw new IOException("API请求失败: " + apiResponse.getMsg());
        }

        return apiResponse.getData();
    }


    public void request(String path, Object body) throws IOException {
        request("POST", path, body, new TypeToken<Void>() {});
    }

    public boolean ping() {
        try {
            get("/api/base/ping", new TypeToken<Void>() {});
            return true;
        } catch (Exception e) {
            logger.warn("Ping failed", e);
            return false;
        }
    }

    public void massLogin(String token) throws IOException {
        request("POST", "/api/security/login", new LoginRequest(token),
            new TypeToken<Void>() {});
    }

    private static class LoginRequest {
        private final String token;

        public LoginRequest(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }

    public final DesktopClient desktop;

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public Gson getGson() {
        return gson;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public static MassClient findAsync(int startPort, int tryTimes) {
        int port = startPort;

        MassClient client = null;
        do {
            try {
                client = new MassClient("http://127.0.0.1:" + port);
                if (client.ping()) {
                    break;
                }
                client = null;
            } catch (Exception e) {
                if (client != null) {
                    client = null;
                }
            }
            port++;
        } while (port < startPort + tryTimes);

        if (client == null) {
            throw new RuntimeException("没有找到 Mass 本地服务");
        }

        return client;
    }
}