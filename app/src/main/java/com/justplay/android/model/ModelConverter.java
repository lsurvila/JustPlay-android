package com.justplay.android.model;

import com.justplay.android.network.response.SearchResponse;

import java.util.ArrayList;
import java.util.List;

public class ModelConverter {

    public MediaGridViewModel toViewModel(MediaGridViewModel model, List<SearchResponse> searchResponseList) {
        List<MediaItemViewModel> items = new ArrayList<>();
        int size = searchResponseList.size();
        for (int i = 0; i < size; i++) {
            SearchResponse searchResponse = searchResponseList.get(i);
            items.add(new MediaItemViewModel(searchResponse.getId(), searchResponse.getTitle(), searchResponse.getImageUrl()));
        }
        model.setGrid(items);
        return model;
    }

    public MediaGridViewModel toViewModel(MediaGridViewModel model, Throwable throwable) {
        model.setErrorMessage(throwable.getLocalizedMessage());
        return model;
    }

}
