package com.mass.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mass.sdk.desktop.DesktopClient;
import com.mass.sdk.desktop.enums.DesktopServerTypeAdapter;
import com.mass.sdk.desktop.enums.DesktopGameVersion;
import com.mass.sdk.desktop.enums.DesktopGameVersionAdapter;
import com.mass.sdk.desktop.enums.DesktopServerStatus;
import com.mass.sdk.desktop.enums.DesktopServerStatusAdapter;
import com.mass.sdk.desktop.enums.DesktopServerType;
import com.mass.sdk.desktop.enums.DesktopVisibilityStatus;
import com.mass.sdk.desktop.enums.DesktopVisibilityStatusAdapter;
import com.mass.sdk.instance.InstanceClient;
import com.mass.sdk.mobile.MobileClient;
import com.mass.sdk.models.ApiResponse;
import com.mass.sdk.models.GameInstance;
import com.mass.sdk.models.MassInstance;
import com.mass.sdk.models.Progress;
import com.mass.sdk.models.account.AccountPlatform;
import com.mass.sdk.models.account.AccountType;
import com.mass.sdk.serialization.AccountPlatformAdapter;
import com.mass.sdk.serialization.AccountTypeAdapter;
import com.mass.sdk.serialization.MassInstanceAdapter;
import com.mass.sdk.serialization.UnixTimestampInstantAdapter;
import com.mass.sdk.signalr.SignalRClient;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Consumer;

public final class MassClient {
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(60);

    private final String baseUrl;
    private final HttpClient httpClient;
    private final Gson gson;
    private final DesktopClient desktop;
    private final MobileClient mobile;
    private final InstanceClient instance;

    public MassClient(String baseUrl) {
        this(baseUrl, defaultHttpClient(), defaultGson());
    }

    public MassClient(String baseUrl, HttpClient httpClient, Gson gson) {
        this.baseUrl = normalizeBaseUrl(baseUrl);
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
        this.gson = Objects.requireNonNull(gson, "gson");
        this.desktop = new DesktopClient(this);
        this.mobile = new MobileClient(this);
        this.instance = new InstanceClient(this);
    }

    public DesktopClient desktop() {
        return desktop;
    }

    public MobileClient mobile() {
        return mobile;
    }

    public InstanceClient instance() {
        return instance;
    }

    public HttpClient httpClient() {
        return httpClient;
    }

    public Gson gson() {
        return gson;
    }

    public String baseUrl() {
        return baseUrl;
    }

    public boolean ping() {
        try {
            request(HttpMethod.GET, "/api/base/ping", Parameters.empty(), TypeToken.get(Void.class));
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    public void massLogin(String token) throws IOException {
        post("/api/security/login", Parameters.create().add("token", token), TypeToken.get(Void.class));
    }

    public GameInstance progress(String path) throws IOException {
        return progress(path, null);
    }

    public GameInstance progress(String path, Consumer<Progress> progressConsumer) throws IOException {
        return new SignalRClient(httpClient, gson, baseUrl).awaitGameInstance(path, progressConsumer);
    }

    public <T> T get(String path, TypeToken<T> typeToken) throws IOException {
        return request(HttpMethod.GET, path, Parameters.empty(), typeToken);
    }

    public <T> T get(String path, Parameters query, TypeToken<T> typeToken) throws IOException {
        return request(HttpMethod.GET, path, query, typeToken);
    }

    public <T> T post(String path, TypeToken<T> typeToken) throws IOException {
        return request(HttpMethod.POST, path, Parameters.empty(), typeToken);
    }

    public <T> T post(String path, Parameters form, TypeToken<T> typeToken) throws IOException {
        return request(HttpMethod.POST, path, form, typeToken);
    }

    public void post(String path, Parameters form) throws IOException {
        post(path, form, TypeToken.get(Void.class));
    }

    public <T> T request(HttpMethod method, String path, Parameters parameters, TypeToken<T> typeToken) throws IOException {
        var request = buildRequest(method, path, parameters == null ? Parameters.empty() : parameters);
        var response = send(request);
        return parseApiResponse(response.body(), typeToken.getType());
    }

    public static MassClient find() {
        return find(23333, 10);
    }

    public static MassClient find(int startPort, int tryTimes) {
        for (var port = startPort; port < startPort + tryTimes; port++) {
            var client = new MassClient("http://127.0.0.1:" + port);
            if (client.ping()) {
                return client;
            }
        }

        throw new IllegalStateException("Mass local service was not found.");
    }

    private HttpRequest buildRequest(HttpMethod method, String path, Parameters parameters) {
        var builder = HttpRequest.newBuilder()
                .timeout(REQUEST_TIMEOUT);

        if (method == HttpMethod.GET) {
            builder.uri(URI.create(baseUrl + path + parameters.toQueryString()));
            builder.GET();
            return builder.build();
        }

        builder.uri(URI.create(baseUrl + path));
        builder.header("Content-Type", "application/x-www-form-urlencoded");
        builder.POST(HttpRequest.BodyPublishers.ofString(parameters.toFormBody()));
        return builder.build();
    }

    private HttpResponse<String> send(HttpRequest request) throws IOException {
        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IOException("HTTP request failed: " + response.statusCode() + " " + response.body());
            }
            return response;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IOException("HTTP request interrupted.", exception);
        }
    }

    private <T> T parseApiResponse(String body, Type dataType) throws IOException {
        var apiResponseType = TypeToken.getParameterized(ApiResponse.class, dataType == Void.class ? Object.class : dataType).getType();
        ApiResponse<?> response = gson.fromJson(body, apiResponseType);

        if (response == null) {
            throw new IOException("Empty API response.");
        }

        if (response.getCode() != 200) {
            throw new IOException(response.getMsg());
        }

        if (dataType == Void.class) {
            return null;
        }

        @SuppressWarnings("unchecked")
        ApiResponse<T> typed = (ApiResponse<T>) response;
        return typed.getData();
    }

    private static String normalizeBaseUrl(String baseUrl) {
        Objects.requireNonNull(baseUrl, "baseUrl");
        var normalized = baseUrl.startsWith("http://") || baseUrl.startsWith("https://")
                ? baseUrl
                : "http://" + baseUrl;
        return normalized.endsWith("/") ? normalized.substring(0, normalized.length() - 1) : normalized;
    }

    private static HttpClient defaultHttpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(CONNECT_TIMEOUT)
                .build();
    }

    private static Gson defaultGson() {
        return new GsonBuilder()
                .registerTypeAdapter(DesktopGameVersion.class, new DesktopGameVersionAdapter())
                .registerTypeAdapter(DesktopServerStatus.class, new DesktopServerStatusAdapter())
                .registerTypeAdapter(DesktopVisibilityStatus.class, new DesktopVisibilityStatusAdapter())
                .registerTypeAdapter(DesktopServerType.class, new DesktopServerTypeAdapter())
                .registerTypeAdapter(Instant.class, new UnixTimestampInstantAdapter())
                .registerTypeAdapter(AccountPlatform.class, new AccountPlatformAdapter())
                .registerTypeAdapter(AccountType.class, new AccountTypeAdapter())
                .registerTypeAdapter(MassInstance.class, new MassInstanceAdapter())
                .create();
    }

    public enum HttpMethod {
        GET,
        POST
    }

    public static final class Parameters {
        private final LinkedHashMap<String, String> values = new LinkedHashMap<>();

        private Parameters() {
        }

        public static Parameters create() {
            return new Parameters();
        }

        public static Parameters empty() {
            return new Parameters();
        }

        public Parameters add(String key, Object value) {
            if (value == null) {
                return this;
            }

            var text = value instanceof Boolean bool
                    ? (bool ? "1" : "0")
                    : String.valueOf(value);

            if (!text.isEmpty()) {
                values.put(key, text);
            }
            return this;
        }

        public Parameters addAll(Map<String, ?> entries) {
            entries.forEach(this::add);
            return this;
        }

        public boolean isEmpty() {
            return values.isEmpty();
        }

        public String toQueryString() {
            if (isEmpty()) {
                return "";
            }

            return "?" + encodedPairs();
        }

        public String toFormBody() {
            return encodedPairs();
        }

        private String encodedPairs() {
            var joiner = new StringJoiner("&");
            values.forEach((key, value) -> joiner.add(encode(key) + "=" + encode(value)));
            return joiner.toString();
        }

        private static String encode(String value) {
            try {
                return URLEncoder.encode(value, StandardCharsets.UTF_8);
            } catch (Exception exception) {
                throw new UncheckedIOException(new IOException("Failed to encode parameter: " + value, exception));
            }
        }
    }
}
