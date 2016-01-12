package com.justplay.android.component;

import com.justplay.android.ApplicationComponent;
import com.justplay.android.module.MediaDownloadModule;
import com.justplay.android.presenter.MediaDownloadPresenter;
import com.justplay.android.scope.MediaScope;

import dagger.Component;

@MediaScope
@Component(
        dependencies = { ApplicationComponent.class },
        modules = { MediaDownloadModule.class }
)
public interface MediaDownloadComponent {
    MediaDownloadPresenter downloadPresenter();
}
