package com.justplay.android.module;

import com.justplay.android.model.ModelConverter;
import com.justplay.android.scope.MediaScope;
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

    @Provides @MediaScope
    MediaSearchView provideMediaSearchView() {
        return view;
    }

    @Provides @MediaScope
    MediaSearchPresenter provideMediaSearchPresenter(MediaSearchView view, JustPlayApi api, ModelConverter modelConverter) {
        return new MediaSearchPresenter(view, api, modelConverter);
    }

}
