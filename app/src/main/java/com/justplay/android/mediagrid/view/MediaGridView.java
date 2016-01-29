package com.justplay.android.mediagrid.view;

import com.justplay.android.mediagrid.model.MediaItemViewModel;

import java.util.List;

public interface MediaGridView {
    void updateGrid(List<MediaItemViewModel> items);
    void setProgressBarVisible(boolean visible);
    void setGridVisible(boolean visible);
    void invalidateItemState(int position);
    void showSnackbar(String message);
    void showToast(String message);
}
