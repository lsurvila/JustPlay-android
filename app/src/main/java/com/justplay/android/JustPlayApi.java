package com.justplay.android;

import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;

class JustPlayApi {

    private static final String BASE_URL = "http://justplay-web.com";
    private final JustPlayService service;

    public JustPlayApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(JustPlayService.class);
    }

    public Observable<List<SearchResponse>> search(String query) {
        return service.search(query);
    }

}
