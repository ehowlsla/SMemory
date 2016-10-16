package com.myspringway.secretmemory.presenter;

import android.util.Log;

import com.myspringway.secretmemory.activity.JoinChurchActivity;
import com.myspringway.secretmemory.activity.JoinNameActivity;
import com.myspringway.secretmemory.service.RestService;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yoontaesup on 2016. 10. 16..
 */

public class JoinNamePresenter {
    JoinNameActivity activity;
    RestService service;

    public JoinNamePresenter(JoinNameActivity activity, RestService service) {
        this.activity = activity;
        this.service = service;
    }

    public void getName(String name) {
        service.getApi()
                .getName(name)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> activity.callbackSuccess(res),
                        e -> Log.d("restService", e.getMessage()),
                        () -> Log.d("restService", "onCompleted"));
    }
}
