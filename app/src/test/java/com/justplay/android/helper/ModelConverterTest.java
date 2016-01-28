package com.justplay.android.helper;

import com.justplay.android.mediagrid.model.MediaGridViewModel;
import com.justplay.android.mediagrid.model.MediaItemViewModel;
import com.justplay.android.network.response.SearchResponse;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelConverterTest {

    private ModelConverter modelConverter;

    @Before
    public void setUp() throws Exception {
        modelConverter = new ModelConverter();
    }

    @Test
    public void shouldConvertSuccessfulSearchResponse() throws Exception {
        MediaGridViewModel viewModel = new MediaGridViewModel();
        List<SearchResponse> searchResponseList = new ArrayList<>();
        searchResponseList.add(new SearchResponse("34", "Adele - Hello", "https://i.ytimg.com/vi/Nck6BZga7TQ/hqdefault.jpg"));
        searchResponseList.add(new SearchResponse("12", "Taylor Swift - Shake it Off", "https://i.ytimg.com/vi/NkBZwer/hqdefault.jpg"));
        searchResponseList.add(new SearchResponse("443", "Dreams", "https://i.ytimg.com/vi/df3411/hqdefault.jpg"));

        viewModel = modelConverter.toViewModel(viewModel, searchResponseList);

        assertThat(viewModel.isSuccessful()).isTrue();
        assertMediaItemViewModel("34", "Adele - Hello", "https://i.ytimg.com/vi/Nck6BZga7TQ/hqdefault.jpg", viewModel.getGrid().get(0));
        assertMediaItemViewModel("12", "Taylor Swift - Shake it Off", "https://i.ytimg.com/vi/NkBZwer/hqdefault.jpg", viewModel.getGrid().get(1));
        assertMediaItemViewModel("443", "Dreams", "https://i.ytimg.com/vi/df3411/hqdefault.jpg", viewModel.getGrid().get(2));
    }

    @SuppressWarnings("ThrowableInstanceNeverThrown")
    @Test
    public void shouldConvertFailedSearchResponse() throws Exception {
        MediaGridViewModel viewModel = new MediaGridViewModel();
        Throwable throwable = new Throwable("Some Error");

        viewModel = modelConverter.toViewModel(viewModel, throwable);

        assertThat(viewModel.isSuccessful()).isFalse();
        assertThat(viewModel.getErrorMessage()).isEqualTo("Some Error");
    }

    private static void assertMediaItemViewModel(String id, String title, String imageUrl, MediaItemViewModel model) {
        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getTitle()).isEqualTo(title);
        assertThat(model.getImageUrl()).isEqualTo(imageUrl);
    }


}