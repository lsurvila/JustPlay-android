package com.justplay.android.external.network.response;

import com.google.gson.annotations.Expose;

public class SearchResponse {

    @Expose
    private final String id;

    @Expose
    private final String title;

    @Expose
    private final String imageUrl;

    public SearchResponse(String id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
