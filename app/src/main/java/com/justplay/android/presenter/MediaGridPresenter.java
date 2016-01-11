package com.justplay.android.presenter;

import com.justplay.android.network.JustPlayApi;
import com.justplay.android.network.response.SearchResponse;
import com.justplay.android.view.MediaGridView;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.RxLifecycle;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MediaGridPresenter {

    private final MediaGridView view;
    private final JustPlayApi api;

    public MediaGridPresenter(MediaGridView view, JustPlayApi api) {
        this.view = view;
        this.api = api;
    }

    public void downloadMediaItem(int position, SearchResponse item) {
        item.setIsDownloading(true);
        view.invalidateItemState(position);
        api.download(item.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycle.bindUntilFragmentEvent(view.getLifecycle(), FragmentEvent.DESTROY))
                .subscribe(file -> {
                    item.setIsDownloading(false);
                    view.invalidateItemState(position);
                    view.showSnackbar("Downloaded to " + file.getAbsolutePath());
                }, error -> {
                    item.setIsDownloading(false);
                    view.invalidateItemState(position);
                    view.showToast(error.getLocalizedMessage());
                });
    }



}
