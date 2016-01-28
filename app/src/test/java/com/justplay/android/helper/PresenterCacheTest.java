package com.justplay.android.helper;

import com.justplay.android.mediagrid.presenter.MediaGridPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PresenterCacheTest {

    private PresenterCache presenterCache;

    @Before
    public void setUp() throws Exception {
        presenterCache = new PresenterCache();
    }

    @Test
    public void shouldUnbindOldPresenterWhenNewIsSet() throws Exception {
        MediaGridPresenter oldPresenter = mock(MediaGridPresenter.class);
        presenterCache.savePresenter(oldPresenter);

        presenterCache.savePresenter(mock(MediaGridPresenter.class));

        verify(oldPresenter).unbindView();
    }

    @Test
    public void shouldRemovePresenter() throws Exception {
        MediaGridPresenter presenter = mock(MediaGridPresenter.class);
        presenterCache.savePresenter(presenter);

        presenterCache.removePresenter();

        verify(presenter, Mockito.times(2)).unbindView();
        verify(presenter).unsubscribe();
        assertThat(presenterCache.getPresenter()).isNull();
    }

}