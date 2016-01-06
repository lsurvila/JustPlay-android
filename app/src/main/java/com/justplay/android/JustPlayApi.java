package com.justplay.android;

import android.content.Context;
import android.os.Environment;

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
    private static final String DOWNLOAD_PATH = "video/download?id=";
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

    public Observable<File> download(String id) {
        return service.download(id)
                .map(response -> {
                    File file = null;
                    try {
                        String header = response.headers().get("Content-Disposition");
                        String fileName = header.replace("attachment; filename=", "");
                        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsoluteFile(), fileName);
                        BufferedSink sink = Okio.buffer(Okio.sink(file));
                        sink.writeAll(response.body().source());
                        sink.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return file;
                });
    }

}
