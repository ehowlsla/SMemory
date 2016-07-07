package com.myspringway.secretmemory.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myspringway.secretmemory.R;

import butterknife.ButterKnife;

/**
 * Created by yoontaesup on 2016. 6. 11..
 */
public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            goRequestCardList();
        } else {
            goLoginActivity();
        }
    }

    private void goRequestCardList() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                overridePendingTransition(0, android.R.anim.fade_in);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 300);
    }


    private void goLoginActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                overridePendingTransition(0,android.R.anim.fade_in);
                startActivity(new Intent(SplashActivity.this, SignActivity.class));
                finish();
            }
        }, 300);
    }
}
