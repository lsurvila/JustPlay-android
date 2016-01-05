package com.justplay.android;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

interface JustPlayService {

    @GET("/video/search")
    Observable<List<SearchResponse>> search(@Query("q") String query);

}
