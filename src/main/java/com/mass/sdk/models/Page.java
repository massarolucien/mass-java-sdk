package com.mass.sdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public final class Page<T> {
    @SerializedName("items")
    private List<T> items = new ArrayList<>();
    @SerializedName("total_page")
    private int totalPage;

    public List<T> getItems() {
        return items;
    }

    public Page<T> withItems(List<T> items) {
        this.items = items == null ? new ArrayList<>() : items;
        return this;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public Page<T> withTotalPage(int totalPage) {
        this.totalPage = totalPage;
        return this;
    }
}
