package com.myspringway.secretmemory.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.myspringway.secretmemory.library.SoftKeyboard;
import com.myspringway.secretmemory.model.Member;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by yoontaesup on 2016. 6. 11..
 */

public class JoinPasswordActivity extends Activity {

    private static final String TAG = JoinPasswordActivity.class.getSimpleName();

    private static final int PASSWORD_MIN = 6;

    @BindView(R.id.password)
    ClearableEditText password;

    @BindView(R.id.password_re)
    ClearableEditText password_re;

    @BindView(R.id.prev)
    TextView prev;

    @BindView(R.id.next)
    TextView next;

    @BindView(R.id.root_layout)
    RelativeLayout root_layout;

    SoftKeyboard softKeyboard;

    private DatabaseReference mDataRef;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_password);
        ButterKnife.bind(this);

        mDataRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        initObject();
    }

    private void initObject() {
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);

        softKeyboard = new SoftKeyboard(root_layout, im);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
                setVisibleBottom(true);
                passwordCheck(true);
            }

            @Override
            public void onSoftKeyboardShow() {
                setVisibleBottom(false);
            }
        });

    }

    public void setVisibleBottom(final boolean isVisible) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                prev.setVisibility(isVisible ? View.VISIBLE : View.GONE);
                next.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(softKeyboard != null) softKeyboard.unRegisterSoftKeyboardCallback();
    }

    @OnTextChanged(R.id.password)
    void passwordTextChange(CharSequence text, int start, int before, int count) {
        passwordCheck(false);
    }

    @OnTextChanged(R.id.password_re)
    void passwordReTextChange(CharSequence text, int start, int before, int count) {
        passwordCheck(false);
    }


    public void passwordCheck(final boolean toast) {
        runOnUiThread(() -> {
            if (password.getText().toString().equals(password_re.getText().toString()) && password.getText().toString().length() >= 6) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    next.setBackground(getDrawable(R.drawable.green_click));
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    next.setBackground(getResources().getDrawable(R.drawable.green_click));
                } else {
                    next.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_click));
                }
                next.setEnabled(true);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    next.setBackground(getDrawable(R.color.black_20));
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    next.setBackground(getResources().getDrawable(R.color.black_20));
                } else {
                    next.setBackgroundDrawable(getResources().getDrawable(R.color.black_20));
                }
                if (toast) {
                    if (password.getText().toString().length() < PASSWORD_MIN)
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_length), Toast.LENGTH_SHORT).show();
                    else if (password.getText().toString().matches("((?=.*\\d)(?=.*[a-z]).{6,20})"))
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_pattern), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
                }
                next.setEnabled(false);
            }
        });
    }

    @OnClick(R.id.prev)
    void prevPage() {
        finish();
    }

    @OnClick(R.id.next)
    void nextPage() {
        if (password.getText().toString().equals(password_re.getText().toString()) && password.getText().toString().length() >= 4) {
            goJoinRequest();
        }
    }

    void goJoinRequest() {
        startLoading();

        String email = SharedPreferenceHelper.getValue(this, AppConstant.EMAIL);;
        String pw = password.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(task -> {
                    stopLoading();
                    if(task.isSuccessful()) {
                        onAuthSuccess(task.getResult().getUser());
                        goProfileActivity();
//                        goMainActivity();
                        finish();
                    } else {
                        Log.e(TAG, "가입 실패");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, e.getMessage());
                    handleErrorMessage(e.getMessage().trim());
                });
    }

    void handleErrorMessage(String errorString) {
        Log.d(TAG, getString(R.string.error_eng_duplicate));
        if (errorString.equals(getString(R.string.error_eng_duplicate))) {
            Toast.makeText(JoinPasswordActivity.this, getString(R.string.error_kor_duplicate), Toast.LENGTH_SHORT).show();
            finish();
        }
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

    private void onAuthSuccess(FirebaseUser user) {
        writeNewUser(user.getUid(), user.getEmail());
    }
    private void writeNewUser(String userId, String email) {
        Member member = new Member(email);
        member.name = SharedPreferenceHelper.getValue(getApplicationContext(), AppConstant.NAME);
        member.pastor = SharedPreferenceHelper.getValue(getApplicationContext(), AppConstant.PASTOR);
        mDataRef.child("members").child(userId).setValue(member);
    }

    PopupLoading popupLoading;

    void goProfileActivity() {
        SharedPreferenceHelper.setValue(getApplicationContext(), AppConstant.PASSWORD, password.getText().toString());

        Intent intent = new Intent(getApplicationContext(), JoinProfileActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }

    void goMainActivity() {
        SharedPreferenceHelper.setValue(getApplicationContext(), AppConstant.PASSWORD, password.getText().toString());

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }

}
