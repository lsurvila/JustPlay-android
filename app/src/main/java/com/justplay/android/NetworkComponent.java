package com.justplay.android;

import com.justplay.android.presenter.MediaSearchPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class})
public interface NetworkComponent {
    void injectPresenter(MediaSearchPresenter presenter);
}
