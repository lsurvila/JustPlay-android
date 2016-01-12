package com.justplay.android.dagger;

import com.justplay.android.presenter.MediaSearchPresenter;

import dagger.Component;

@PerActivity
@Component(
        dependencies = { ApplicationComponent.class },
        modules = { MediaSearchModule.class }
)
public interface MediaSearchComponent {
    MediaSearchPresenter presenter();
}
