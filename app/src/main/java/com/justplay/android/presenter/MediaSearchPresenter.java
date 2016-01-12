package com.justplay.android.presenter;

import com.jakewharton.rxbinding.support.v7.widget.SearchViewQueryTextEvent;
import com.justplay.android.network.JustPlayApi;
import com.justplay.android.view.MediaSearchView;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.RxLifecycle;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MediaSearchPresenter {

    @Inject JustPlayApi api;
    private final MediaSearchView view;

    public MediaSearchPresenter(MediaSearchView view) {
        this.view = view;
    }

    // TODO on error will stop chain, resolve
    public void searchMediaOnSubmit(Observable<SearchViewQueryTextEvent> queryTextEvents) {
        queryTextEvents
                .flatMap(event -> {
                    if (event.isSubmitted()) {
                        return api.search(event.queryText().toString())
                                .subscribeOn(Schedulers.io());
                    }
                    return Observable.empty();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycle.bindUntilActivityEvent(view.getLifecycle(), ActivityEvent.DESTROY))
                .subscribe(view::updateGrid, error -> view.showToast(error.getLocalizedMessage()));
    }

}
