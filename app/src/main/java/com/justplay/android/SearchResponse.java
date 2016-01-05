package com.justplay.android;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class SearchResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

}
