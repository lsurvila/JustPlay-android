package com.justplay.android.helper;

import com.justplay.android.mediagrid.presenter.MediaGridPresenter;

public class PresenterCache {

    private MediaGridPresenter presenter;

    public void savePresenter(MediaGridPresenter presenter) {
        this.presenter = presenter;
    }

    public void removePresenter() {
        this.presenter = null;
    }

    public MediaGridPresenter getPresenter() {
        return presenter;
    }

}
