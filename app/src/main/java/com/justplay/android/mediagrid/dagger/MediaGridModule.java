package com.justplay.android.mediagrid.dagger;

import com.justplay.android.helper.AndroidPermissionChecker;
import com.justplay.android.mediagrid.model.MediaGridViewModel;
import com.justplay.android.helper.ModelConverter;
import com.justplay.android.helper.PermissionManager;
import com.justplay.android.mediagrid.presenter.MediaGridPresenter;
import com.justplay.android.network.JustPlayApi;
import com.justplay.android.mediagrid.view.adapter.MediaItemAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaGridModule {

    @Provides @MediaScope
    MediaItemAdapter provideMediaItemAdapter() {
        return new MediaItemAdapter();
    }

    @Provides @MediaScope
    AndroidPermissionChecker permissionChecker() {
        return new AndroidPermissionChecker();
    }

    @Provides @MediaScope
    PermissionManager providePermissionManager(AndroidPermissionChecker permissionChecker) {
        return new PermissionManager(permissionChecker);
    }

    @Provides @MediaScope
    MediaGridViewModel provideViewModel() {
        return new MediaGridViewModel();
    }

    @Provides @MediaScope
    MediaGridPresenter provideMediaGridPresenter(MediaGridViewModel model, JustPlayApi api, ModelConverter modelConverter) {
        return new MediaGridPresenter(model, api, modelConverter);
    }

}
