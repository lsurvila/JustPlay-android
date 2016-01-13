package com.justplay.android.module;

import com.justplay.android.permission.PermissionManager;
import com.justplay.android.scope.MediaScope;
import com.justplay.android.network.JustPlayApi;
import com.justplay.android.presenter.MediaDownloadPresenter;
import com.justplay.android.view.MediaGridView;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaDownloadModule {

    private final MediaGridView view;

    public MediaDownloadModule(MediaGridView view) {
        this.view = view;
    }

    @Provides @MediaScope
    MediaGridView provideMediaGridView() {
        return view;
    }

    @Provides @MediaScope
    PermissionManager providePermissionManager(MediaGridView view) {
        return new PermissionManager(view);
    }

    @Provides @MediaScope
    MediaDownloadPresenter provideMediaDownloadPresenter(MediaGridView view, JustPlayApi api, PermissionManager permissionManager) {
        return new MediaDownloadPresenter(view, api, permissionManager);
    }

}
