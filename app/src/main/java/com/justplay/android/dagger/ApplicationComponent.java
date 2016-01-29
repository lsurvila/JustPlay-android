package com.justplay.android.dagger;

import com.justplay.android.helper.ModelConverter;
import com.justplay.android.helper.PresenterCache;
import com.justplay.android.external.network.JustPlayApi;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { ApplicationModule.class })
public interface ApplicationComponent {
    JustPlayApi justPlayApi();
    PresenterCache presenterCache();
    ModelConverter modelConverter();
}
