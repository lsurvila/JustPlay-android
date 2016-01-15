package com.justplay.android.presenter;

import com.jakewharton.rxbinding.support.v7.widget.SearchViewQueryTextEvent;
import com.justplay.android.model.MediaGridViewModel;
import com.justplay.android.model.MediaItemViewModel;
import com.justplay.android.model.ModelConverter;
import com.justplay.android.network.JustPlayApi;
import com.justplay.android.permission.PermissionManager;
import com.justplay.android.view.MediaGridView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MediaGridPresenter implements PermissionManager.PermissionCallback {

    private MediaGridView view;
    private final JustPlayApi api;
    private final PermissionManager permissionManager;
    private final ModelConverter modelConverter;
    private final MediaGridViewModel model;

    private List<Subscription> subscriptionList = new ArrayList<>();

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
        subscriptionList.add(api.download(item.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> {
                    item.setIsDownloading(false);
                    if (view != null) {
                        view.invalidateItemState(position);
                        view.showSnackbar("Downloaded to " + file.getAbsolutePath());
                    }
                }, error -> {
                    item.setIsDownloading(false);
                    if (view != null) {
                        view.invalidateItemState(position);
                        view.showToast(error.getLocalizedMessage());
                    }
                }));
    }


    public void requestDownload(int position) {
        permissionManager.requestPermissionIfNeeded(position);
    }


    public void handlePermissionResponse(int requestCode, int[] grantResults) {
        permissionManager.handleResponse(requestCode, grantResults);
    }

    @Override
    public void onPermissionGranted(int requestCode) {
        downloadMediaItem(requestCode, model.getGrid().get(requestCode));
    }

    @Override
    public void onPermissionDenied() {
        view.showToast("In order to save media files to disk, permission must be turned on");
    }

    public void searchMediaOnSubmit(Observable<SearchViewQueryTextEvent> queryTextEvents) {
        subscriptionList.add(queryTextEvents
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
        unbindView();
        int size = subscriptionList.size();
        for (int i = 0; i < size; i++) {
            subscriptionList.get(i).unsubscribe();
        }
    }

}
