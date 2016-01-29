package com.justplay.android.mediagrid.presenter;

import com.justplay.android.external.repository.MediaGridRepository;
import com.justplay.android.mediagrid.model.MediaGridViewModel;
import com.justplay.android.mediagrid.model.MediaItemViewModel;
import com.justplay.android.mediagrid.view.MediaGridView;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MediaGridPresenter {

    private static final int MAX_DOWNLOADS = 5;
    private MediaGridView view;

    private final MediaGridViewModel model;
    private final MediaGridRepository repository;
    private final Scheduler observeOnScheduler;

    private final CompositeSubscription subscription = new CompositeSubscription();

    private int requestCount;

    @Inject
    public MediaGridPresenter(MediaGridViewModel model, MediaGridRepository repository, Scheduler observeOnScheduler) {
        this.model = model;
        this.repository = repository;
        this.observeOnScheduler = observeOnScheduler;
    }

    public void bindView(MediaGridView view) {
        this.view = view;
    }

    public void unbindView() {
        view = null;
    }

    public void searchMedia(Observable<String> query) {
        subscription.add(query
                .flatMap(searchQuery -> {
                    model.setSearching(true);
                    updateViewVisibility();
                    return repository.searchMedia(model, searchQuery);
                })
                .observeOn(observeOnScheduler)
                .subscribe(result -> {
                    model.setSearching(false);
                    updateViewState();
                }, Throwable::printStackTrace));
    }

    private void downloadMediaItem(int position, MediaItemViewModel item) {
        item.setIsDownloading(true);
        view.invalidateItemState(position);
        subscription.add(repository.downloadVideo(item.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(observeOnScheduler)
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

    public void downloadMediaItemAllowed(int position) {
        if (model.getGrid().get(position).isDownloading()) {
            view.showToast(model.getGrid().get(position).getTitle() + " is already downloading");
        } else if (requestCount < MAX_DOWNLOADS) {
            requestCount++;
            downloadMediaItem(position, model.getGrid().get(position));
        } else {
            // TODO implement queue, try executor services on either retrofit/rxjava
            // TODO server will now return 4-20min audio files, however having large file causes OOM
            // TODO streaming solution will be introduced anyway (work along on these issues)
            view.showToast("Too many requests, only " + MAX_DOWNLOADS + " requests allowed at the same time.");
        }
    }

    public void downloadMediaItemDenied() {
        view.showToast("In order to save media files to disk, permission must be turned on");
    }


    public void updateViewState() {
        if (view != null) {
            updateViewVisibility();
            if (model.isSuccessful()) {
                view.updateGrid(model.getGrid());
            } else {
                view.showToast(model.getErrorMessage());
            }
        }
    }

    private void updateViewVisibility() {
        view.setProgressBarVisible(model.isSearching());
        view.setGridVisible(!model.isSearching());
    }


    public void unsubscribe() {
        subscription.unsubscribe();
    }

}
