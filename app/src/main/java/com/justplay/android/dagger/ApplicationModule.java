package com.justplay.android.dagger;

import com.justplay.android.JustPlayApplication;
import com.justplay.android.network.JustPlayApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private JustPlayApplication application;

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

}
