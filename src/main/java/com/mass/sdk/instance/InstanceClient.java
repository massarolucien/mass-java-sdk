package com.mass.sdk.instance;

import com.google.gson.reflect.TypeToken;
import com.mass.sdk.MassClient;
import com.mass.sdk.models.MassInstance;

import java.io.IOException;
import java.util.List;

public final class InstanceClient {
    private final MassClient client;

    public InstanceClient(MassClient client) {
        this.client = client;
    }

    public List<MassInstance> getList() throws IOException {
        return client.get("/api/instance/list", new TypeToken<>() {});
    }

    public void closeAll() throws IOException {
        client.post("/api/instance/close-all", MassClient.Parameters.empty());
    }

    public void close(String gameId, String roleName) throws IOException {
        client.post("/api/instance/close",
                MassClient.Parameters.create()
                        .add("gameId", gameId)
                        .add("roleName", roleName));
    }

    public void close(long instanceId) throws IOException {
        client.post("/api/instance/" + instanceId + "/close", MassClient.Parameters.empty());
    }
}
