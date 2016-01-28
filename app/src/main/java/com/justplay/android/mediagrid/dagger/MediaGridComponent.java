package com.justplay.android.mediagrid.dagger;

import com.justplay.android.dagger.ApplicationComponent;
import com.justplay.android.mediagrid.presenter.MediaGridPresenter;
import com.justplay.android.mediagrid.view.fragment.MainActivityFragment;

import dagger.Component;

@MediaScope
@Component(
        dependencies = { ApplicationComponent.class },
        modules = { MediaGridModule.class }
)
public interface MediaGridComponent {
    void inject(MainActivityFragment fragment);
    MediaGridPresenter mediaGridPresenter();
}
