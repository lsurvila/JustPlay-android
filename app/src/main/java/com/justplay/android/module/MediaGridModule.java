package com.justplay.android.module;

import com.justplay.android.model.ModelConverter;
import com.justplay.android.permission.PermissionManager;
import com.justplay.android.presenter.MediaGridPresenter;
import com.justplay.android.scope.MediaScope;
import com.justplay.android.network.JustPlayApi;
import com.justplay.android.view.MediaGridView;
import com.justplay.android.view.adapter.MediaItemAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaGridModule {

    private final MediaGridView view;

    public MediaGridModule(MediaGridView view) {
        this.view = view;
    }

    @Provides @MediaScope
    MediaGridView provideMediaGridView() {
        return view;
    }

    @Provides @MediaScope
    MediaItemAdapter provideMediaItemAdapter() {
        return new MediaItemAdapter();
    }

    @Provides @MediaScope
    PermissionManager providePermissionManager(MediaGridView view) {
        return new PermissionManager(view);
    }

    @Provides @MediaScope
    MediaGridPresenter provideMediaGridPresenter(MediaGridView view, JustPlayApi api, PermissionManager permissionManager, ModelConverter modelConverter) {
        return new MediaGridPresenter(view, api, permissionManager, modelConverter);
    }

}
