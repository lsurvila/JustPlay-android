package com.justplay.android.network;

import com.justplay.android.network.response.SearchResponse;
import com.squareup.okhttp.ResponseBody;

import java.util.List;

import retrofit.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

interface JustPlayService {

    @GET("/video/search")
    Observable<List<SearchResponse>> search(@Query("q") String query);

    @GET("/video/download")
    Observable<Response<ResponseBody>> download(@Query("id") String id);

}
