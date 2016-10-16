package com.myspringway.secretmemory.presenter;

import android.util.Log;

import com.myspringway.secretmemory.activity.JoinChurchActivity;
import com.myspringway.secretmemory.service.RestService;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yoontaesup on 2016. 10. 16..
 */

public class JoinChurchPresenter {
    JoinChurchActivity activity;
    RestService service;

    public JoinChurchPresenter(JoinChurchActivity activity, RestService service) {
        this.activity = activity;
        this.service = service;
    }

    public void getPastor(String pastor) {

        service.getApi()
                .getPastor(pastor)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> activity.callbackSuccess(res),
                        e -> Log.d("restService", e.getMessage()),
                        () -> Log.d("restService", "onCompleted"));
    }
}
