package com.mass.sdk;

import java.util.List;

public class Page<T> {
    private List<T> items;
    private int totalPage;

    public Page() {}

    public Page(List<T> items, int totalPage) {
        this.items = items;
        this.totalPage = totalPage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "Page{" +
                "items=" + items +
                ", totalPage=" + totalPage +
                '}';
    }
}