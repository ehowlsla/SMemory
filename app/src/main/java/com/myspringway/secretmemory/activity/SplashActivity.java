package com.myspringway.secretmemory.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.constant.AppConstant;
import com.myspringway.secretmemory.helper.SharedPreferenceHelper;

import butterknife.ButterKnife;

/**
 * Created by yoontaesup on 2016. 6. 11..
 */
public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        String email = SharedPreferenceHelper.getValue(this, AppConstant.EMAIL);;
        String password = SharedPreferenceHelper.getValue(this, AppConstant.PASSWORD);;

        mAuth.getCurrentUser().linkWithCredential(EmailAuthProvider.getCredential(email, password))
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mUser = task.getResult().getUser();
                        } else {
                            Toast.makeText(SplashActivity.this, getResources().getString(R.string.error_sign_in_wrong_info), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = mAuth.getCurrentUser();
                if (mUser != null) {
                    goRequestCardList();
                } else {
                    goLoginActivity();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
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
