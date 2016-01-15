package com.justplay.android.model;

import java.util.List;

public class MediaGridViewModel {

    private List<MediaItemViewModel> grid;
    private String errorMessage;
    private boolean isSearching;

    public void setGrid(List<MediaItemViewModel> grid) {
        this.grid = grid;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setSearching(boolean searching) {
        isSearching = searching;
    }

    public boolean isSuccessful() {
        return errorMessage == null;
    }

    public List<MediaItemViewModel> getGrid() {
        return grid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSearching() {
        return isSearching;
    }

}
