package com.justplay.android;

import com.justplay.android.view.MediaSearchView;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaSearchModule {

    private final MediaSearchView view;

    public MediaSearchModule(MediaSearchView view) {
        this.view = view;
    }

    @Provides
    MediaSearchView provideMediaSearchView() {
        return view;
    }

}
