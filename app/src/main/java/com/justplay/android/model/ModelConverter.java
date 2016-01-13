package com.justplay.android.model;

import com.justplay.android.network.response.SearchResponse;

import java.util.ArrayList;
import java.util.List;

public class ModelConverter {

    public MediaGridViewModel toViewModel(List<SearchResponse> searchResponseList) {
        List<MediaItemViewModel> items = new ArrayList<>();
        int size = searchResponseList.size();
        for (int i = 0; i < size; i++) {
            SearchResponse searchResponse = searchResponseList.get(i);
            items.add(new MediaItemViewModel(searchResponse.getId(), searchResponse.getTitle(), searchResponse.getImageUrl()));
        }
        return new MediaGridViewModel(items);
    }

    public MediaGridViewModel toViewModel(Throwable throwable) {
        return new MediaGridViewModel(throwable.getLocalizedMessage());
    }

}
