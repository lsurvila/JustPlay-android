package com.justplay.android.presenter;

import com.jakewharton.rxbinding.support.v7.widget.SearchViewQueryTextEvent;
import com.justplay.android.model.MediaGridViewModel;
import com.justplay.android.model.MediaItemViewModel;
import com.justplay.android.model.ModelConverter;
import com.justplay.android.network.JustPlayApi;
import com.justplay.android.permission.PermissionManager;
import com.justplay.android.view.MediaGridView;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.RxLifecycle;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MediaGridPresenter implements PermissionManager.Callback {

    private final MediaGridView view;
    private final JustPlayApi api;
    private final PermissionManager permissionManager;
    private final ModelConverter modelConverter;

    private MediaItemViewModel requestedItem;
    private int requestedItemPosition;

    @Inject
    public MediaGridPresenter(MediaGridView view, JustPlayApi api, PermissionManager permissionManager, ModelConverter modelConverter) {
        this.view = view;
        this.api = api;
        this.permissionManager = permissionManager;
        this.permissionManager.setCallback(this);
        this.modelConverter = modelConverter;
    }

    private void downloadMediaItem(int position, MediaItemViewModel item) {
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


    public void requestDownload(int position, MediaItemViewModel item) {
        requestedItemPosition = position;
        requestedItem = item;
        permissionManager.requestPermissionIfNeeded();
    }


    public void handlePermissionResponse(int requestCode, int[] grantResults) {
        permissionManager.handleResponse(requestCode, grantResults);
    }

    @Override
    public void onPermissionGranted() {
        downloadMediaItem(requestedItemPosition, requestedItem);
    }

    @Override
    public void onPermissionDenied() {
        view.showToast("In order to save media files to disk, permission must be turned on");
    }


    public void searchMediaOnSubmit(Observable<SearchViewQueryTextEvent> queryTextEvents) {
        queryTextEvents
                .flatMap(this::performSearchOnSubmit)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycle.bindUntilFragmentEvent(view.getLifecycle(), FragmentEvent.DESTROY))
                .subscribe(model -> {
                    view.hideProgressBar();
                    if (model.isSuccessful()) {
                        view.updateGrid(model.getGrid());
                    } else {
                        view.showToast(model.getErrorMessage());
                    }
                });
    }

    private Observable<MediaGridViewModel> performSearchOnSubmit(SearchViewQueryTextEvent event) {
        if (event.isSubmitted()) {
            view.showProgressBar();
            return api.search(event.queryText().toString())
                    .map(modelConverter::toViewModel)
                    .onErrorResumeNext(throwable -> Observable.just(modelConverter.toViewModel(throwable)))
                    .subscribeOn(Schedulers.io());
        }
        return Observable.empty();
    }

}
