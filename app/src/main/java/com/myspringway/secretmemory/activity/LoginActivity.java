package com.myspringway.secretmemory.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.constant.AppConstant;
import com.myspringway.secretmemory.constants.HttpUrl;
import com.myspringway.secretmemory.dialog.PopupLoading;
import com.myspringway.secretmemory.helper.SharedPreferenceHelper;
import com.myspringway.secretmemory.library.ClearableEditText;
import com.myspringway.secretmemory.model.Member;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yoontaesup on 2016. 6. 11..
 */

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    private DatabaseReference mDataRef;
    private FirebaseAuth mAuth;
    private Firebase ref;

    @BindView(R.id.email)
    ClearableEditText email;

    @BindView(R.id.password)
    ClearableEditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Firebase.setAndroidContext(this);
        ref = new Firebase(HttpUrl.FIREBASE_ENDPOINT);
        mDataRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        email.setText(SharedPreferenceHelper.getValue(getApplicationContext(), AppConstant.EMAIL));
        password.setText(SharedPreferenceHelper.getValue(getApplicationContext(), AppConstant.PASSWORD));
    }

    @OnClick(R.id.facebook_login)
    void goFacebookLogin() {

    }

    @OnClick(R.id.login)
    void goLogin() {
        if (!validateForm()) { return; }

        startLoading();

        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        stopLoading();
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.error_sign_in_wrong_info), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.getCause().getMessage());
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.error_sign_in_transmit), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError(getResources().getString(R.string.error_sign_in_blank_id));
            result = false;
        } else {
            email.setError(null);
        }

        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError(getResources().getString(R.string.error_sign_in_blank_pw));
            result = false;
        } else {
            password.setError(null);
        }

        return result;
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        writeNewUser(user.getUid(), username, user.getEmail());
        goMainActivity();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        Member member = new Member(name, email);

        mDataRef.child("members").child(userId).setValue(member);
    }

    PopupLoading popupLoading;
    private void startLoading() {
        popupLoading = new PopupLoading();
        popupLoading.show(getFragmentManager(), "loading");
    }

    private void stopLoading() {
        if(popupLoading != null) {
            popupLoading.dismiss();
        }
    }

    @OnClick(R.id.join)
    void goJoin() {
        Intent intent = new Intent(this, JoinEmailActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.find)
    void goFind() {

    }

    void goMainActivity() {
        SharedPreferenceHelper.setValue(getApplicationContext(), AppConstant.EMAIL, email.getText().toString().trim());
        SharedPreferenceHelper.setValue(getApplicationContext(), AppConstant.PASSWORD, password.getText().toString().trim());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
