package com.justplay.android.view;

import com.trello.rxlifecycle.FragmentEvent;

public interface MediaGridView extends RxLifecycleView<FragmentEvent> {
    void invalidateItemState(int position);
    void showSnackbar(String message);
    void showToast(String message);
}
