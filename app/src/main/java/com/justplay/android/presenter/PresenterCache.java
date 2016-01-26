package com.justplay.android.presenter;

import com.justplay.android.JustPlayApplication;
import com.justplay.android.component.DaggerMediaGridComponent;
import com.justplay.android.component.MediaGridComponent;

public class PresenterCache {

    private final MediaGridComponent component;
    private MediaGridPresenter presenter;

    public PresenterCache() {
        component = DaggerMediaGridComponent.builder()
                .applicationComponent(JustPlayApplication.component())
                .build();
    }

    public void savePresenter(MediaGridPresenter presenter) {
        // we need to unbind current view, as after activity/fragment restarts, new view will be provided
        presenter.unbindView();
        this.presenter = presenter;
    }

    public void removePresenter() {
        // at this point activity/fragment will be destroyed for good, so we need to remove presenter (if it's saved)
        // and unsubscribe all ongoing operations if any.
        if (this.presenter != null) {
            this.presenter.unsubscribe();
            this.presenter.unbindView();
        }
        this.presenter = null;
    }

    public MediaGridPresenter getPresenter() {
        if (presenter == null) {
            presenter = component.gridPresenter();
        }
        return presenter;
    }

}
