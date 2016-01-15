package com.justplay.android.presenter;

import com.jakewharton.rxbinding.support.v7.widget.SearchViewQueryTextEvent;
import com.justplay.android.model.MediaGridViewModel;
import com.justplay.android.model.MediaItemViewModel;
import com.justplay.android.model.ModelConverter;
import com.justplay.android.network.JustPlayApi;
import com.justplay.android.permission.PermissionManager;
import com.justplay.android.view.MediaGridView;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MediaGridPresenter implements PermissionManager.PermissionCallback {

    private static final int MAX_DOWNLOADS = 5;
    private MediaGridView view;
    private final JustPlayApi api;
    private final PermissionManager permissionManager;
    private final ModelConverter modelConverter;
    private final MediaGridViewModel model;

    private final CompositeSubscription subscription = new CompositeSubscription();

    private int requestCount;

    @Inject
    public MediaGridPresenter(MediaGridViewModel model, JustPlayApi api, PermissionManager permissionManager, ModelConverter modelConverter) {
        this.model = model;
        this.api = api;
        this.permissionManager = permissionManager;
        this.permissionManager.setCallback(this);
        this.modelConverter = modelConverter;
    }

    public void bindView(MediaGridView view) {
        this.view = view;
        this.permissionManager.bindFragmentOrActivity(view);
    }

    public void unbindView() {
        view = null;
    }

    private void downloadMediaItem(int position, MediaItemViewModel item) {
        item.setIsDownloading(true);
        view.invalidateItemState(position);
        subscription.add(api.download(item.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> {
                    item.setIsDownloading(false);
                    if (view != null) {
                        view.invalidateItemState(position);
                        view.showSnackbar("Downloaded to " + file.getAbsolutePath());
                    }
                    requestCount--;
                }, error -> {
                    item.setIsDownloading(false);
                    if (view != null) {
                        view.invalidateItemState(position);
                        view.showToast(error.getLocalizedMessage());
                    }
                    requestCount--;
                }));
    }

    public void requestDownload(int position) {
        if (model.getGrid().get(position).isDownloading()) {
            view.showToast(model.getGrid().get(position).getTitle() + " is already downloading");
        } else if (requestCount < MAX_DOWNLOADS) {
            permissionManager.requestPermissionIfNeeded(position);
        } else {
            // TODO implement queue, try executor services on either retrofit/rxjava
            // TODO server will now return 4-20min audio files, however having large file causes OOM
            // TODO streaming solution will be introduced anyway (work along on these issues)
            view.showToast("Too many requests, only " + MAX_DOWNLOADS + " requests allowed at the same time.");
        }
    }

    public void handlePermissionResponse(int requestCode, int[] grantResults) {
        permissionManager.handleResponse(requestCode, grantResults);
    }

    @Override
    public void onPermissionGranted(int requestCode) {
        requestCount++;
        downloadMediaItem(requestCode, model.getGrid().get(requestCode));
    }

    @Override
    public void onPermissionDenied() {
        view.showToast("In order to save media files to disk, permission must be turned on");
    }

    public void searchMediaOnSubmit(Observable<SearchViewQueryTextEvent> queryTextEvents) {
        subscription.add(queryTextEvents
                .flatMap(this::performSearchOnSubmit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    view.hideProgressBar();
                    view.showGrid();
                    model.setSearching(false);
                    if (model.isSuccessful()) {
                        view.updateGrid(model.getGrid());
                    } else {
                        view.showToast(model.getErrorMessage());
                    }
                }));
    }

    private Observable<MediaGridViewModel> performSearchOnSubmit(SearchViewQueryTextEvent event) {
        if (event.isSubmitted()) {
            view.showProgressBar();
            view.hideGrid();
            model.setSearching(true);
            return api.search(event.queryText().toString())
                    .map(items -> modelConverter.toViewModel(model, items))
                    .onErrorResumeNext(throwable -> Observable.just(modelConverter.toViewModel(model, throwable)))
                    .subscribeOn(Schedulers.io());
        }
        return Observable.empty();
    }

    public void restoreViewState() {
        if (model.isSearching()) {
            view.showProgressBar();
            view.hideGrid();
        } else {
            view.hideProgressBar();
            view.showGrid();
            view.updateGrid(model.getGrid());
        }
    }

    public void unsubscribe() {
        view.showToast("Cancelling requests");
        subscription.unsubscribe();
    }

}
