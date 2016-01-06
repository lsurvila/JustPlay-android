package com.justplay.android;

import android.content.Context;
import android.os.Environment;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okio.BufferedSink;
import okio.Okio;
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

    public Observable<File> downloadMediaFile(Context context, String fileUrl, String fileName) {
        return Observable.create(subscriber -> {
            OkHttpClient client = new OkHttpClient();
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName);
            Request request = new Request.Builder().url(fileUrl).build();
            Response response;
            try {
                response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    throw new IOException();
                }
                BufferedSink sink = Okio.buffer(Okio.sink(file));
                sink.writeAll(response.body().source());
                sink.close();
                subscriber.onNext(file);
                subscriber.onCompleted();
            } catch (IOException io) {
                subscriber.onError(io);
            }
        });
    }

}
