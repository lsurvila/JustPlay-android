package com.justplay.android;

import dagger.Component;

@Component(modules = { ApplicationModule.class })
public interface ApplicationComponent {
    void inject(JustPlayApplication application);
}
