package com.justplay.android.dagger;

import com.justplay.android.JustPlayApplication;
import com.justplay.android.external.repository.MediaGridRepository;
import com.justplay.android.helper.ModelConverter;
import com.justplay.android.helper.PresenterCache;
import com.justplay.android.external.network.JustPlayApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final JustPlayApplication application;

    public ApplicationModule(JustPlayApplication application) {
        this.application = application;
    }

    @Provides @Singleton
    JustPlayApplication provideApplication() {
        return application;
    }

    @Provides @Singleton
    JustPlayApi provideJustPlayApi() {
        return new JustPlayApi();
    }

    @Provides @Singleton
    ModelConverter provideModelConverter() {
        return new ModelConverter();
    }

    @Provides @Singleton
    MediaGridRepository provideMediaGridRepository(JustPlayApi api, ModelConverter modelConverter) {
        return new MediaGridRepository(api, modelConverter);
    }

    @Provides @Singleton
    PresenterCache providePresenterCache() {
        return new PresenterCache();
    }

}
