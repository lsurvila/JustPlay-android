package com.justplay.android.view;

import com.justplay.android.model.MediaItemViewModel;

import java.util.List;

public interface MediaGridView {
    void updateGrid(List<MediaItemViewModel> items);
    void showGrid();
    void hideGrid();
    void showProgressBar();
    void hideProgressBar();
    void invalidateItemState(int position);
    void showSnackbar(String message);
    void showToast(String message);
}
