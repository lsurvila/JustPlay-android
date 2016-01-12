package com.justplay.android.dagger;

import com.justplay.android.network.JustPlayApi;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { ApplicationModule.class })
public interface ApplicationComponent {
    JustPlayApi api();
}
