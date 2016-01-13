package com.justplay.android.presenter;

import com.jakewharton.rxbinding.support.v7.widget.SearchViewQueryTextEvent;
import com.justplay.android.model.ModelConverter;
import com.justplay.android.network.JustPlayApi;
import com.justplay.android.view.MediaSearchView;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.RxLifecycle;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MediaSearchPresenter {

    private final JustPlayApi api;
    private final MediaSearchView view;
    private final ModelConverter modelConverter;

    @Inject
    public MediaSearchPresenter(MediaSearchView view, JustPlayApi api, ModelConverter modelConverter) {
        this.api = api;
        this.view = view;
        this.modelConverter = modelConverter;
    }

    public void searchMediaOnSubmit(Observable<SearchViewQueryTextEvent> queryTextEvents) {
        queryTextEvents
                .flatMap(event -> {
                    if (event.isSubmitted()) {
                        return api.search(event.queryText().toString())
                                .map(modelConverter::toViewModel)
                                .onErrorResumeNext(throwable -> Observable.just(modelConverter.toViewModel(throwable)))
                                .subscribeOn(Schedulers.io());
                    }
                    return Observable.empty();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycle.bindUntilActivityEvent(view.getLifecycle(), ActivityEvent.DESTROY))
                .subscribe(model -> {
                    if (model.isSuccessful()) {
                        view.updateGrid(model.getGrid());
                    } else {
                        view.showToast(model.getErrorMessage());
                    }
                });
    }

}
