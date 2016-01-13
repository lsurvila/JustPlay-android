package com.justplay.android.network.response;

import com.google.gson.annotations.Expose;

public class SearchResponse {

    @Expose
    private String id;

    @Expose
    private String title;

    @Expose
    private String imageUrl;

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
