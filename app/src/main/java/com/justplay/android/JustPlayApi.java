package com.justplay.android;

import android.os.Environment;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okio.BufferedSink;
import okio.Okio;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;

class JustPlayApi {

    private static final String BASE_URL = "http://murmuring-harbor-8639.herokuapp.com";
    private final JustPlayService service;

    public JustPlayApi() {
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client.interceptors().add(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
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
                .flatMap(this::saveFile);
    }

    public Observable<File> saveFile(Response<ResponseBody> response) {
        return Observable.create((Observable.OnSubscribe<File>) subscriber -> {
            try {
                String header = response.headers().get("Content-Disposition");
                String fileName = header.replace("attachment; filename=", "");
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsoluteFile(), fileName);
                BufferedSink sink = Okio.buffer(Okio.sink(file));
                sink.writeAll(response.body().source());
                sink.close();
                subscriber.onNext(file);
                subscriber.onCompleted();
            } catch (IOException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });
    }

}
