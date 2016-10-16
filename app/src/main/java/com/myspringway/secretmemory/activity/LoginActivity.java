package com.myspringway.secretmemory.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.constants.AppConstant;
import com.myspringway.secretmemory.dialog.PopupLoading;
import com.myspringway.secretmemory.helper.SharedPreferenceHelper;
import com.myspringway.secretmemory.library.ClearableEditText;
import com.myspringway.secretmemory.model.Member;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    private DatabaseReference mDataRef;
    private FirebaseAuth mAuth;
    private PopupLoading popupLoading;

    @BindView(R.id.email)
    ClearableEditText email;

    @BindView(R.id.password)
    ClearableEditText password;

    private boolean isTwoClickBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mDataRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        email.setText(SharedPreferenceHelper.getValue(getApplicationContext(), AppConstant.EMAIL));
        password.setText(SharedPreferenceHelper.getValue(getApplicationContext(), AppConstant.PASSWORD));
    }

    @Override
    public void onBackPressed(){

        if (!isTwoClickBack) {
            Toast.makeText(this, getResources().getString(R.string.back_finish),
                    Toast.LENGTH_SHORT).show();
            CntTimer timer = new CntTimer(2000, 1);
            timer.start();
            return;
        } else {
            finish();
            return;
        }
    }

    class CntTimer extends CountDownTimer {
        public CntTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            isTwoClickBack = true;
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            isTwoClickBack = false;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub

        }
    }

    @OnClick(R.id.facebook_login)
    void goFacebookLogin() {
        Toast.makeText(getApplicationContext(), R.string.function_no_active, Toast.LENGTH_SHORT).show();
    }

    void goKakaoTalkLogin() {
        // TODO: ADD KAKAOTALK LOGIN LOGIC
    }

    @OnClick(R.id.login)
    void goLogin() {

        if (!validateForm()) { return; }

        startLoading();

        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        onAuthSuccess(task.getResult().getUser());
                        stopLoading();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    stopLoading();
                    Log.e(TAG, e.getMessage());
                    handleErrorMsg(e.getMessage());
                });
    }

    private void handleErrorMsg(String errorMsg) {
        if (errorMsg.equals(getString(R.string.error_eng_wrong_password))) {
            Toast.makeText(LoginActivity.this, getString(R.string.error_kor_wrong_password), Toast.LENGTH_SHORT).show();
        }
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
        Member member = new Member(email);

        mDataRef.child("members").child(userId).setValue(member);
    }

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
        Intent intent = new Intent(this, JoinChurchActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.find)
    void goFind() {
        Toast.makeText(getApplicationContext(), R.string.function_no_active, Toast.LENGTH_SHORT).show();
    }

    void goMainActivity() {
        SharedPreferenceHelper.setValue(getApplicationContext(), AppConstant.EMAIL, email.getText().toString().trim());
        SharedPreferenceHelper.setValue(getApplicationContext(), AppConstant.PASSWORD, password.getText().toString().trim());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
