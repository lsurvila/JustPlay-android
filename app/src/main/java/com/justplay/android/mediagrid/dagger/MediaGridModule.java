package com.justplay.android.mediagrid.dagger;

import com.justplay.android.external.repository.MediaGridRepository;
import com.justplay.android.helper.AndroidPermissionChecker;
import com.justplay.android.mediagrid.model.MediaGridViewModel;
import com.justplay.android.helper.PermissionManager;
import com.justplay.android.mediagrid.presenter.MediaGridPresenter;
import com.justplay.android.mediagrid.view.adapter.MediaItemAdapter;

import dagger.Module;
import dagger.Provides;
import rx.android.schedulers.AndroidSchedulers;

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
    MediaGridPresenter provideMediaGridPresenter(MediaGridViewModel model, MediaGridRepository repository) {
        return new MediaGridPresenter(model, repository, AndroidSchedulers.mainThread());
    }

}
