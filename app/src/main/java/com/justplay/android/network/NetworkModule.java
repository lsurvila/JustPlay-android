package com.justplay.android.network;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {

    @Provides @Singleton
    JustPlayApi provideJustPlayApi() {
        return new JustPlayApi();
    }

}
