package com.justplay.android.view;

import com.justplay.android.network.response.SearchResponse;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.List;

import rx.Observable;

public interface MediaGridView {
    void updateGrid(List<SearchResponse> searchResponses);
    void showToast(String message);
    Observable<ActivityEvent> getLifecycle();
}
