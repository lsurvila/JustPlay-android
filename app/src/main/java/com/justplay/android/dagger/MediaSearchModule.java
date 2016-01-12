package com.justplay.android.dagger;

import com.justplay.android.network.JustPlayApi;
import com.justplay.android.presenter.MediaSearchPresenter;
import com.justplay.android.view.MediaSearchView;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaSearchModule {

    private final MediaSearchView view;

    public MediaSearchModule(MediaSearchView view) {
        this.view = view;
    }

    @Provides @PerActivity
    MediaSearchView provideMediaSearchView() {
        return view;
    }

    @Provides @PerActivity
    MediaSearchPresenter provideMediaSearchPresenter(MediaSearchView view, JustPlayApi api) {
        return new MediaSearchPresenter(api, view);
    }

}
