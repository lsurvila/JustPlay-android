package com.justplay.android.model;

import java.util.List;

public class MediaGridViewModel {

    private List<MediaItemViewModel> grid;
    private String errorMessage;

    public MediaGridViewModel(List<MediaItemViewModel> grid) {
        this.grid = grid;
    }

    public MediaGridViewModel(String errorMessage) {
        this.errorMessage = errorMessage;
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

}
