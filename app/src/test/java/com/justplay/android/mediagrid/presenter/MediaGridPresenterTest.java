package com.justplay.android.mediagrid.presenter;

import com.justplay.android.external.repository.MediaGridRepository;
import com.justplay.android.mediagrid.model.MediaGridViewModel;
import com.justplay.android.mediagrid.model.MediaItemViewModel;
import com.justplay.android.mediagrid.view.MediaGridView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MediaGridPresenterTest {

    private MediaGridPresenter mediaGridPresenter;

    @Mock
    MediaGridView view;

    @Mock
    MediaGridViewModel model;

    @Mock
    MediaGridRepository repository;


    @Before
    public void setUp() throws Exception {
        model = new MediaGridViewModel();
        mediaGridPresenter = new MediaGridPresenter(model, repository, Schedulers.immediate());
        mediaGridPresenter.bindView(view);
    }

    @Test
    public void shouldShowProgressBarWhenSearchIsSubmitted() throws Exception {
        String query = "test";

        mediaGridPresenter.searchMedia(Observable.just(query));

        verify(view).setProgressBarVisible(true);
        verify(view).setGridVisible(false);
    }

    @Test
    public void shouldShowGridWhenSearchIsSuccessful() throws Exception {
        String query = "test";
        List<MediaItemViewModel> modelList = new ArrayList<>();
        modelList.add(new MediaItemViewModel("45", "Adele", "url1"));
        modelList.add(new MediaItemViewModel("46", "Disco", "url2"));
        model.setGrid(modelList);
        when(repository.searchMedia(model, query)).thenReturn(Observable.just(model));

        mediaGridPresenter.searchMedia(Observable.just(query));

        verify(view).setProgressBarVisible(false);
        verify(view).setGridVisible(true);
        verify(view).updateGrid(modelList);
    }

    @Test
    public void shouldShowErrorToastWhenSearchIsUnsuccessful() throws Exception {
        String query = "test";
        String error = "error";
        model.setErrorMessage(error);
        when(repository.searchMedia(model, query)).thenReturn(Observable.just(model));

        mediaGridPresenter.searchMedia(Observable.just(query));

        verify(view).setProgressBarVisible(false);
        verify(view).setGridVisible(true);
        verify(view).showToast(error);
    }

}