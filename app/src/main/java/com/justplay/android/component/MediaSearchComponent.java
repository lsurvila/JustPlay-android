package com.justplay.android.component;

import com.justplay.android.ApplicationComponent;
import com.justplay.android.scope.MediaScope;
import com.justplay.android.module.MediaSearchModule;
import com.justplay.android.presenter.MediaSearchPresenter;

import dagger.Component;

@MediaScope
@Component(
        dependencies = { ApplicationComponent.class },
        modules = { MediaSearchModule.class }
)
public interface MediaSearchComponent {
    MediaSearchPresenter searchPresenter();
}
