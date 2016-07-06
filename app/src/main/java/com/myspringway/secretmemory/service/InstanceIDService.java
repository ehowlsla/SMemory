package com.myspringway.secretmemory.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by legab on 2016-06-19.
 * This is required if you
 */
public class InstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "InstanceIDService";

    /* The onTokenRefresh callback fires whenever a new token is generated,
     * so calling getToken in this content ensures that you are accessing a current,
     * available registration token.
     * The token may be rotated whenever. */
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
}
