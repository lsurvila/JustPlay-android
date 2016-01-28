package com.justplay.android.helper;

import com.justplay.android.mediagrid.presenter.MediaGridPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PresenterCacheTest {

    @Mock
    MediaGridPresenter presenter;

    private PresenterCache presenterCache;

    @Before
    public void setUp() {
        presenterCache = new PresenterCache();
    }

    @Test
    public void shouldSavePresenter() {
        presenterCache.savePresenter(presenter);

        assertThat(presenterCache.getPresenter()).isNotNull();
        assertThat(presenterCache.getPresenter()).isEqualTo(presenter);
    }

    @Test
    public void shouldRemovePresenter() {
        presenterCache.savePresenter(presenter);
        presenterCache.removePresenter();

        assertThat(presenterCache.getPresenter()).isNull();
    }

}