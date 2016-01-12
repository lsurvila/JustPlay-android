package com.justplay.android;

import android.app.Application;

public class JustPlayApplication extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        component.inject(this);
    }

    public ApplicationComponent getComponent() {
        return component;
    }
    
}
