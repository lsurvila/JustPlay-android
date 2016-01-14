package com.justplay.android.view;

import com.justplay.android.model.MediaItemViewModel;
import com.trello.rxlifecycle.FragmentEvent;

import java.util.List;

public interface MediaGridView extends RxLifecycleView<FragmentEvent> {
    void updateGrid(List<MediaItemViewModel> items);
    void showProgressBar();
    void hideProgressBar();
    void invalidateItemState(int position);
    void showSnackbar(String message);
    void showToast(String message);
}
