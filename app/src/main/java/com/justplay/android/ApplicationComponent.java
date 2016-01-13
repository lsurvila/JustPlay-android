package com.justplay.android;

import com.justplay.android.model.ModelConverter;
import com.justplay.android.network.JustPlayApi;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { ApplicationModule.class })
public interface ApplicationComponent {
    JustPlayApi api();
    ModelConverter converter();
}
