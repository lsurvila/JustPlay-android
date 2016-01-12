package com.justplay.android;

import com.justplay.android.network.NetworkModule;
import com.justplay.android.presenter.MediaSearchPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class, MediaSearchModule.class})
public interface MediaSearchComponent {
    void injectPresenter(MediaSearchPresenter presenter);
}
