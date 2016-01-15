package com.justplay.android.presenter;

public class PresenterCache {

    private MediaGridPresenter presenter;

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
        return presenter;
    }

}
