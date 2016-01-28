package com.justplay.android.mediagrid.model;

public class MediaItemViewModel {

    private final String id;
    private final String title;
    private final String imageUrl;
    private boolean isDownloading;

    public MediaItemViewModel(String id, String title, String imageUrl) {
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

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setIsDownloading(boolean isDownloading) {
        this.isDownloading = isDownloading;
    }

}
