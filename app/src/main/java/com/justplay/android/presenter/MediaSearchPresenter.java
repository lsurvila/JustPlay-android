package com.justplay.android.presenter;

import com.jakewharton.rxbinding.support.v7.widget.SearchViewQueryTextEvent;
import com.justplay.android.network.JustPlayApi;
import com.justplay.android.network.response.SearchResponse;
import com.justplay.android.view.MediaSearchView;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.RxLifecycle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MediaSearchPresenter {

    private final JustPlayApi api;
    private final MediaSearchView view;

    @Inject
    public MediaSearchPresenter(MediaSearchView view, JustPlayApi api) {
        this.api = api;
        this.view = view;
    }

    public void searchMediaOnSubmit(Observable<SearchViewQueryTextEvent> queryTextEvents) {
        queryTextEvents
                .flatMap(event -> {
                    if (event.isSubmitted()) {
                        return api.search(event.queryText().toString())
                                .onErrorResumeNext(throwable -> {
                                    List<SearchResponse> list = new ArrayList<>();
                                    SearchResponse searchResponse = new SearchResponse();
                                    searchResponse.setErrorMessage(throwable.getLocalizedMessage());
                                    list.add(searchResponse);
                                    return Observable.just(list);
                                })
                                .subscribeOn(Schedulers.io());
                    }
                    return Observable.empty();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycle.bindUntilActivityEvent(view.getLifecycle(), ActivityEvent.DESTROY))
                .subscribe(response -> {
                    if (response.get(0).getErrorMessage() != null) {
                        view.showToast(response.get(0).getErrorMessage());
                    } else {
                        view.updateGrid(response);
                    }
                });
    }

}
