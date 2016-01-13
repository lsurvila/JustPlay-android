package com.justplay.android.presenter;

import com.justplay.android.network.JustPlayApi;
import com.justplay.android.network.response.SearchResponse;
import com.justplay.android.permission.PermissionManager;
import com.justplay.android.view.MediaGridView;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.RxLifecycle;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MediaDownloadPresenter implements PermissionManager.Callback {

    private final MediaGridView view;
    private final JustPlayApi api;
    private final PermissionManager permissionManager;

    private SearchResponse requestedItem;
    private int requestedItemPosition;

    @Inject
    public MediaDownloadPresenter(MediaGridView view, JustPlayApi api, PermissionManager permissionManager) {
        this.view = view;
        this.api = api;
        this.permissionManager = permissionManager;
        this.permissionManager.setCallback(this);
    }

    private void downloadMediaItem(int position, SearchResponse item) {
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


    public void requestDownload(int position, SearchResponse item) {
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

}
