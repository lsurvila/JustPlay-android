package com.justplay.android.module;

import com.justplay.android.model.MediaGridViewModel;
import com.justplay.android.model.ModelConverter;
import com.justplay.android.permission.PermissionManager;
import com.justplay.android.presenter.MediaGridPresenter;
import com.justplay.android.scope.MediaScope;
import com.justplay.android.network.JustPlayApi;
import com.justplay.android.view.adapter.MediaItemAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaGridModule {

    @Provides @MediaScope
    MediaItemAdapter provideMediaItemAdapter() {
        return new MediaItemAdapter();
    }

    @Provides @MediaScope
    PermissionManager providePermissionManager() {
        return new PermissionManager();
    }

    @Provides @MediaScope
    MediaGridViewModel provideViewModel() {
        return new MediaGridViewModel();
    }

    @Provides @MediaScope
    MediaGridPresenter provideMediaGridPresenter(MediaGridViewModel model, JustPlayApi api, PermissionManager permissionManager, ModelConverter modelConverter) {
        return new MediaGridPresenter(model, api, permissionManager, modelConverter);
    }

}
