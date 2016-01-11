package com.justplay.android.view;

import com.justplay.android.network.response.SearchResponse;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.List;

public interface MediaSearchView extends RxLifecycleView<ActivityEvent> {
    void updateGrid(List<SearchResponse> searchResponses);
    void showToast(String message);
}
