package com.justplay.android.component;

import com.justplay.android.ApplicationComponent;
import com.justplay.android.module.MediaGridModule;
import com.justplay.android.presenter.MediaGridPresenter;
import com.justplay.android.scope.MediaScope;
import com.justplay.android.view.adapter.MediaItemAdapter;

import dagger.Component;

@MediaScope
@Component(
        dependencies = { ApplicationComponent.class },
        modules = { MediaGridModule.class }
)
public interface MediaGridComponent {
    MediaGridPresenter gridPresenter();
    MediaItemAdapter mediaAdapter();
}
