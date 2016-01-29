package com.justplay.android.external.repository;

import com.justplay.android.external.network.JustPlayApi;
import com.justplay.android.helper.ModelConverter;
import com.justplay.android.mediagrid.model.MediaGridViewModel;

import java.io.File;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public class MediaGridRepository {

    private final ModelConverter modelConverter;
    private final JustPlayApi api;

    @Inject
    public MediaGridRepository(JustPlayApi api, ModelConverter modelConverter) {
        this.api = api;
        this.modelConverter = modelConverter;
    }

    public Observable<MediaGridViewModel> searchMedia(MediaGridViewModel model, String text) {
        return api.search(text)
                .map(items -> modelConverter.toViewModel(model, items))
                .onErrorResumeNext(throwable -> Observable.just(modelConverter.toViewModel(model, throwable)))
                .subscribeOn(Schedulers.io());
    }

    public Observable<File> downloadVideo(String id) {
        return api.download(id);
    }

}
