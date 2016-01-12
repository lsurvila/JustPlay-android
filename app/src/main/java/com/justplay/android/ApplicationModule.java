package com.justplay.android;

import android.app.Application;

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
    Application provideApplication() {
        return application;
    }

}
