package com.justplay.android.mediagrid.presenter;

import com.justplay.android.helper.ModelConverter;
import com.justplay.android.mediagrid.model.MediaGridViewModel;
import com.justplay.android.mediagrid.view.MediaGridView;
import com.justplay.android.network.JustPlayApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MediaGridPresenterTest {

    private MediaGridViewModel model;

    @Mock
    MediaGridView view;

    private MediaGridPresenter mediaGridPresenter;

    @Mock
    JustPlayApi justPlayApi;

    @Before
    public void setUp() throws Exception {
        model = new MediaGridViewModel();
        mediaGridPresenter = new MediaGridPresenter(model, justPlayApi, new ModelConverter());
        mediaGridPresenter.bindView(view);
    }

    @Test
    public void shouldShowProgressBarWhenSearchIsSubmitted() throws Exception {
        String query = "test";
        when(justPlayApi.search(query)).thenReturn(Observable.empty());

        mediaGridPresenter.searchMediaOnSubmit(true, query);

        verify(view).showProgressBar();
        verify(view).hideGrid();
    }

    @Test
    public void shouldNotShowProgressBarWhenSearchIsNotSubmitted() throws Exception {
        String query = "test";
        when(justPlayApi.search(query)).thenReturn(Observable.empty());

        mediaGridPresenter.searchMediaOnSubmit(false, query);

        verify(view, never()).showProgressBar();
        verify(view, never()).hideGrid();
    }

    @Test
    public void shouldModelBeSetToDownloadingWhenSearchIsSubmitted() throws Exception {
        String query = "test";
        when(justPlayApi.search(query)).thenReturn(Observable.empty());

        mediaGridPresenter.searchMediaOnSubmit(true, query);

        assertThat(model.isSearching()).isTrue();
    }

}