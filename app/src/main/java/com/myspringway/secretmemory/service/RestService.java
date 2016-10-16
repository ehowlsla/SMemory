package com.myspringway.secretmemory.service;

import android.content.Context;

import com.myspringway.secretmemory.model.thirdparty.ResPastor;
import com.myspringway.secretmemory.model.thirdparty.ResName;
import com.myspringway.secretmemory.session.PersistentCookieJar;
import com.myspringway.secretmemory.session.cache.SetCookieCache;
import com.myspringway.secretmemory.session.persistence.SharedPrefsCookiePersistor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by yoontaesup on 2016. 10. 16..
 */

public class RestService {


    private static final String SERVER_URL = "http://info.housechurchministries.org/";


    private RestApi mApi;

    public RestService(Context context) {
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context))).build();
        mApi =  new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RestApi.class);
    }

    public RestApi getApi() { return mApi; }


    public interface RestApi {
        @GET("seminar/api/getHouseChurchApi.php/complete/{name}")
        Observable<ResName> getName(@Path("name") String name);

        @GET("seminar/api/getHouseChurchApi.php/pastor/{name}")
        Observable<ResPastor> getPastor(@Path("name") String name);
    }
}
