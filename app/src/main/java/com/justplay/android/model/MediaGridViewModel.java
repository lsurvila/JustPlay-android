package com.justplay.android.model;

import java.util.List;

public class MediaGridViewModel {

    private List<MediaItemViewModel> grid;
    private String errorMessage;
    private boolean isSearching;
    private int requestItemPosition;

    public void setGrid(List<MediaItemViewModel> grid) {
        this.grid = grid;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setSearching(boolean searching) {
        isSearching = searching;
    }

    public void setRequestItemPosition(int requestItemPosition) {
        this.requestItemPosition = requestItemPosition;
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

    public int getRequestItemPosition() {
        return requestItemPosition;
    }

    public MediaItemViewModel getRequestedItem() {
        return grid.get(requestItemPosition);
    }

}
