package com.justplay.android;

import android.app.Application;

import com.justplay.android.dagger.ApplicationComponent;
import com.justplay.android.dagger.ApplicationModule;
import com.justplay.android.dagger.DaggerApplicationComponent;

public class JustPlayApplication extends Application {

    private static ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        if (component == null) {
            component = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
    }

    public static ApplicationComponent component() {
        return component;
    }

}
