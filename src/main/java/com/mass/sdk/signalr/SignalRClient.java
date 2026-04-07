package com.mass.sdk.signalr;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.mass.sdk.models.GameInstance;
import com.mass.sdk.models.Progress;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public final class SignalRClient {
    private static final String RECORD_SEPARATOR = "\u001e";
    private static final Duration NEGOTIATE_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration HANDSHAKE_TIMEOUT = Duration.ofSeconds(30);

    private final HttpClient httpClient;
    private final Gson gson;
    private final String baseUrl;

    public SignalRClient(HttpClient httpClient, Gson gson, String baseUrl) {
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
        this.gson = Objects.requireNonNull(gson, "gson");
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
    }

    public GameInstance awaitGameInstance(String path, Consumer<Progress> progressConsumer) throws IOException {
        var endpoint = HubEndpoint.parse(baseUrl, path);
        var negotiation = negotiate(endpoint);
        var listener = new HubListener(gson, progressConsumer);

        WebSocket webSocket;
        try {
            webSocket = httpClient.newWebSocketBuilder()
                    .connectTimeout(HANDSHAKE_TIMEOUT)
                    .buildAsync(URI.create(endpoint.websocketUrl(negotiation.getConnectionReference())), listener)
                    .get(HANDSHAKE_TIMEOUT.toSeconds(), TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IOException("SignalR connection interrupted.", exception);
        } catch (ExecutionException | TimeoutException exception) {
            throw new IOException("Failed to connect to SignalR endpoint.", exception);
        }

        try {
            webSocket.sendText(handshakePayload(), true).get(HANDSHAKE_TIMEOUT.toSeconds(), TimeUnit.SECONDS);
            listener.awaitHandshake();
            return listener.awaitGameInstance();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            closeQuietly(webSocket);
            throw new IOException("SignalR workflow interrupted.", exception);
        } catch (ExecutionException | TimeoutException exception) {
            closeQuietly(webSocket);
            throw new IOException("SignalR workflow failed.", exception);
        }
    }

    private NegotiationResponse negotiate(HubEndpoint endpoint) throws IOException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint.negotiateUrl()))
                .timeout(NEGOTIATE_TIMEOUT)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IOException("SignalR negotiate failed: " + response.statusCode() + " " + response.body());
            }

            NegotiationResponse negotiation = gson.fromJson(response.body(), NegotiationResponse.class);
            if (negotiation == null || negotiation.getConnectionReference().isEmpty()) {
                throw new IOException("SignalR negotiate response did not contain a connection id or token.");
            }

            if (!negotiation.supportsWebSockets()) {
                throw new IOException("SignalR negotiate response does not support WebSockets.");
            }

            return negotiation;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IOException("SignalR negotiate interrupted.", exception);
        }
    }

    private String handshakePayload() {
        return "{\"protocol\":\"json\",\"version\":1}" + RECORD_SEPARATOR;
    }

    private void closeQuietly(WebSocket webSocket) {
        try {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "done").join();
        } catch (Exception ignored) {
        }
    }

    private static final class HubListener implements WebSocket.Listener {
        private final Gson gson;
        private final Consumer<Progress> progressConsumer;
        private final CompletableFuture<Void> handshakeFuture = new CompletableFuture<>();
        private final CompletableFuture<GameInstance> gameInstanceFuture = new CompletableFuture<>();
        private final StringBuilder textBuffer = new StringBuilder();

        private HubListener(Gson gson, Consumer<Progress> progressConsumer) {
            this.gson = gson;
            this.progressConsumer = progressConsumer;
        }

        @Override
        public void onOpen(WebSocket webSocket) {
            webSocket.request(1);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            textBuffer.append(data);

            if (last) {
                var payload = textBuffer.toString();
                textBuffer.setLength(0);
                handlePayload(payload);
            }

            webSocket.request(1);
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            if (!handshakeFuture.isDone()) {
                handshakeFuture.completeExceptionally(new IOException("SignalR connection closed before handshake completed."));
            }

            if (!gameInstanceFuture.isDone()) {
                gameInstanceFuture.completeExceptionally(new IOException("SignalR connection closed: " + reason));
            }

            return CompletableFuture.completedFuture(null);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            if (!handshakeFuture.isDone()) {
                handshakeFuture.completeExceptionally(error);
            }

            if (!gameInstanceFuture.isDone()) {
                gameInstanceFuture.completeExceptionally(error);
            }
        }

        private void handlePayload(String payload) {
            var frames = payload.split(RECORD_SEPARATOR);
            for (var frame : frames) {
                var message = frame.trim();
                if (message.isEmpty()) {
                    continue;
                }

                handleMessage(message);
            }
        }

        private void handleMessage(String message) {
            JsonObject json = gson.fromJson(message, JsonObject.class);
            if (json == null) {
                return;
            }

            if (!json.has("type")) {
                handshakeFuture.complete(null);
                return;
            }

            var messageType = json.get("type").getAsInt();
            if (messageType == 1) {
                handleInvocation(json);
                return;
            }

            if (messageType == 7) {
                var error = json.has("error") ? json.get("error").getAsString() : "SignalR closed by server.";
                completeExceptionally(new IOException(error));
                return;
            }

            if (messageType == 6) {
                if (!handshakeFuture.isDone()) {
                    handshakeFuture.complete(null);
                }
            }
        }

        private void handleInvocation(JsonObject json) {
            if (!handshakeFuture.isDone()) {
                handshakeFuture.complete(null);
            }

            var target = json.has("target") ? json.get("target").getAsString() : "";
            var arguments = json.has("arguments") ? json.getAsJsonArray("arguments") : new JsonArray();

            if ("Progress".equals(target)) {
                handleProgress(arguments);
                return;
            }

            if ("GameInstance".equals(target) && arguments.size() > 0) {
                gameInstanceFuture.complete(gson.fromJson(arguments.get(0), GameInstance.class));
                return;
            }

            if ("Disposed".equals(target)) {
                if (!gameInstanceFuture.isDone()) {
                    completeExceptionally(new IOException("SignalR connection was disposed before a game instance was received."));
                }
            }
        }

        private void handleProgress(JsonArray arguments) {
            if (arguments.size() < 4 || progressConsumer == null) {
                return;
            }

            progressConsumer.accept(new Progress()
                    .withStep(asInt(arguments.get(0)))
                    .withTotal(asInt(arguments.get(1)))
                    .withPercentage(asInt(arguments.get(2)))
                    .withMessage(asString(arguments.get(3))));
        }

        private int asInt(JsonElement element) {
            return element == null || element.isJsonNull() ? 0 : element.getAsInt();
        }

        private String asString(JsonElement element) {
            return element == null || element.isJsonNull() ? "" : element.getAsString();
        }

        private void completeExceptionally(Throwable throwable) {
            if (!handshakeFuture.isDone()) {
                handshakeFuture.completeExceptionally(throwable);
            }

            if (!gameInstanceFuture.isDone()) {
                gameInstanceFuture.completeExceptionally(throwable);
            }
        }

        private void awaitHandshake() throws InterruptedException, ExecutionException, TimeoutException {
            handshakeFuture.get(HANDSHAKE_TIMEOUT.toSeconds(), TimeUnit.SECONDS);
        }

        private GameInstance awaitGameInstance() throws InterruptedException, ExecutionException {
            return gameInstanceFuture.get();
        }
    }

    private static final class HubEndpoint {
        private final String httpBaseUrl;
        private final String webSocketBaseUrl;
        private final String hubPath;
        private final String queryString;

        private HubEndpoint(String httpBaseUrl, String webSocketBaseUrl, String hubPath, String queryString) {
            this.httpBaseUrl = httpBaseUrl;
            this.webSocketBaseUrl = webSocketBaseUrl;
            this.hubPath = hubPath;
            this.queryString = queryString;
        }

        public static HubEndpoint parse(String baseUrl, String path) {
            var normalizedPath = path == null || path.isEmpty() ? "/" : path;
            var separatorIndex = normalizedPath.indexOf('?');
            var hubPath = separatorIndex >= 0 ? normalizedPath.substring(0, separatorIndex) : normalizedPath;
            var query = separatorIndex >= 0 ? normalizedPath.substring(separatorIndex + 1) : "";
            var websocketBaseUrl = baseUrl.startsWith("https://")
                    ? "wss://" + baseUrl.substring("https://".length())
                    : "ws://" + baseUrl.substring("http://".length());
            return new HubEndpoint(baseUrl, websocketBaseUrl, hubPath, query);
        }

        public String negotiateUrl() {
            return withHttpQuery(hubPath + "/negotiate", appendQuery(queryString, "negotiateVersion=1"));
        }

        public String websocketUrl(String connectionToken) {
            return withWebSocketQuery(hubPath, appendQuery(queryString, "id=" + encode(connectionToken)));
        }

        private String withHttpQuery(String path, String query) {
            return httpBaseUrl + path + (query.isEmpty() ? "" : "?" + query);
        }

        private String withWebSocketQuery(String path, String query) {
            return webSocketBaseUrl + path + (query.isEmpty() ? "" : "?" + query);
        }

        private String appendQuery(String source, String tail) {
            if (source == null || source.isEmpty()) {
                return tail;
            }
            return source + "&" + tail;
        }

        private String encode(String value) {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        }
    }

    private static final class NegotiationResponse {
        @SerializedName("connectionId")
        private String connectionId = "";
        @SerializedName("connectionToken")
        private String connectionToken = "";
        @SerializedName("availableTransports")
        private List<AvailableTransport> availableTransports = List.of();

        public String getConnectionReference() {
            if (connectionToken != null && !connectionToken.isEmpty()) {
                return connectionToken;
            }

            return connectionId == null ? "" : connectionId;
        }

        public boolean supportsWebSockets() {
            for (var transport : availableTransports) {
                if (transport != null && transport.isWebSockets()) {
                    return true;
                }
            }
            return false;
        }
    }

    private static final class AvailableTransport {
        @SerializedName("transport")
        private String transport = "";

        public boolean isWebSockets() {
            return "WebSockets".equalsIgnoreCase(transport);
        }
    }
}
