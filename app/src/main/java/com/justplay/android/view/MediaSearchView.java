package com.justplay.android.view;

import com.justplay.android.model.MediaItemViewModel;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.List;

public interface MediaSearchView extends RxLifecycleView<ActivityEvent> {
    void updateGrid(List<MediaItemViewModel> items);
    void showToast(String message);
}
