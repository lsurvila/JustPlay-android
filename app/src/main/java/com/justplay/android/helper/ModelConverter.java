package com.justplay.android.helper;

import android.support.annotation.NonNull;

import com.justplay.android.mediagrid.model.MediaGridViewModel;
import com.justplay.android.mediagrid.model.MediaItemViewModel;
import com.justplay.android.external.network.response.SearchResponse;

import java.util.ArrayList;
import java.util.List;

public class ModelConverter {

    public MediaGridViewModel toViewModel(@NonNull MediaGridViewModel model, @NonNull List<SearchResponse> searchResponseList) {
        List<MediaItemViewModel> items = new ArrayList<>();
        int size = searchResponseList.size();
        for (int i = 0; i < size; i++) {
            SearchResponse searchResponse = searchResponseList.get(i);
            items.add(new MediaItemViewModel(searchResponse.getId(), searchResponse.getTitle(), searchResponse.getImageUrl()));
        }
        model.setGrid(items);
        return model;
    }

    public MediaGridViewModel toViewModel(@NonNull MediaGridViewModel model, @NonNull Throwable throwable) {
        model.setErrorMessage(throwable.getLocalizedMessage());
        return model;
    }

}
