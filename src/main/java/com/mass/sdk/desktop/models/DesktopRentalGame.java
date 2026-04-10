package com.mass.sdk.desktop.models;

import com.google.gson.annotations.SerializedName;
import com.mass.sdk.desktop.enums.DesktopServerStatus;
import com.mass.sdk.desktop.enums.DesktopServerType;
import com.mass.sdk.desktop.enums.DesktopVisibilityStatus;
import com.mass.sdk.desktop.interfaces.IDesktopGame;

public final class DesktopRentalGame implements IDesktopGame {
    @SerializedName("entity_id")
    private String id = "";
    @SerializedName("name")
    private String name = "";
    @SerializedName("player_count")
    private long playerCount;
    @SerializedName("like_num")
    private long likeCount;
    @SerializedName("image_url")
    private String imageUrl = "";
    @SerializedName("server_name")
    private String serverName = "";
    @SerializedName("visibility")
    private DesktopVisibilityStatus visibility = DesktopVisibilityStatus.PUBLIC;
    @SerializedName("has_pwd")
    private boolean hasPassword;
    @SerializedName("server_type")
    private DesktopServerType serverType = DesktopServerType.DOCKER;
    @SerializedName("status")
    private DesktopServerStatus status = DesktopServerStatus.NONE;
    @SerializedName("capacity")
    private long capacity;
    @SerializedName("mc_version")
    private String mcVersion = "";
    @SerializedName("owner_id")
    private long ownerId;
    @SerializedName("world_id")
    private String worldId = "";
    @SerializedName("min_level")
    private String minLevel = "";
    @SerializedName("pvp")
    private boolean pvpEnabled;
    @SerializedName("icon_index")
    private long iconIndex;
    @SerializedName("offset")
    private String offset;

    @Override
    public String getId() {
        return id;
    }

    public DesktopRentalGame withId(String id) {
        this.id = id == null ? "" : id;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public DesktopRentalGame withName(String name) {
        this.name = name == null ? "" : name;
        return this;
    }

    @Override
    public long getPlayerCount() {
        return playerCount;
    }

    public DesktopRentalGame withPlayerCount(long playerCount) {
        this.playerCount = playerCount;
        return this;
    }

    @Override
    public long getLikeCount() {
        return likeCount;
    }

    public DesktopRentalGame withLikeCount(long likeCount) {
        this.likeCount = likeCount;
        return this;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    public DesktopRentalGame withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? "" : imageUrl;
        return this;
    }

    public String getServerName() {
        return serverName;
    }

    public DesktopRentalGame withServerName(String serverName) {
        this.serverName = serverName == null ? "" : serverName;
        return this;
    }

    public DesktopVisibilityStatus getVisibility() {
        return visibility;
    }

    public DesktopRentalGame withVisibility(DesktopVisibilityStatus visibility) {
        this.visibility = visibility == null ? DesktopVisibilityStatus.PUBLIC : visibility;
        return this;
    }

    public boolean hasPassword() {
        return hasPassword;
    }

    public DesktopRentalGame withHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
        return this;
    }

    public DesktopServerType getServerType() {
        return serverType;
    }

    public DesktopRentalGame withServerType(DesktopServerType serverType) {
        this.serverType = serverType == null ? DesktopServerType.DOCKER : serverType;
        return this;
    }

    public DesktopServerStatus getStatus() {
        return status;
    }

    public DesktopRentalGame withStatus(DesktopServerStatus status) {
        this.status = status == null ? DesktopServerStatus.NONE : status;
        return this;
    }

    public long getCapacity() {
        return capacity;
    }

    public DesktopRentalGame withCapacity(long capacity) {
        this.capacity = capacity;
        return this;
    }

    public String getMcVersion() {
        return mcVersion;
    }

    public DesktopRentalGame withMcVersion(String mcVersion) {
        this.mcVersion = mcVersion == null ? "" : mcVersion;
        return this;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public DesktopRentalGame withOwnerId(long ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public String getWorldId() {
        return worldId;
    }

    public DesktopRentalGame withWorldId(String worldId) {
        this.worldId = worldId == null ? "" : worldId;
        return this;
    }

    public String getMinLevel() {
        return minLevel;
    }

    public DesktopRentalGame withMinLevel(String minLevel) {
        this.minLevel = minLevel == null ? "" : minLevel;
        return this;
    }

    public boolean isPvpEnabled() {
        return pvpEnabled;
    }

    public DesktopRentalGame withPvpEnabled(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
        return this;
    }

    public long getIconIndex() {
        return iconIndex;
    }

    public DesktopRentalGame withIconIndex(long iconIndex) {
        this.iconIndex = iconIndex;
        return this;
    }

    public String getOffset() {
        return offset;
    }

    public DesktopRentalGame withOffset(String offset) {
        this.offset = offset;
        return this;
    }
}
