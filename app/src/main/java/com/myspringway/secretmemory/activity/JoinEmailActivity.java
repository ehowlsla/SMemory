package com.myspringway.secretmemory.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.constants.AppConstant;
import com.myspringway.secretmemory.helper.SharedPreferenceHelper;
import com.myspringway.secretmemory.library.ClearableEditText;
import com.myspringway.secretmemory.library.SoftKeyboard;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinEmailActivity extends Activity {

    private static final String TAG = JoinEmailActivity.class.getSimpleName();

    @BindView(R.id.email)
    ClearableEditText email;

    @BindView(R.id.prev)
    TextView prev;

    @BindView(R.id.next)
    TextView next;

    @BindView(R.id.root_layout)
    RelativeLayout root_layout;

    private SoftKeyboard mSoftKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_email);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initObject();
    }

    private void initObject() {
        email.setEmailType(true);

        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);

        mSoftKeyboard = new SoftKeyboard(root_layout, im);
        mSoftKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
                setVisibleBottom(true);
                emailExistCheck();
            }

            @Override
            public void onSoftKeyboardShow() {
                setVisibleBottom(false);
            }
        });
    }

    private void setVisibleBottom(final boolean isVisible) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                prev.setVisibility(isVisible ? View.VISIBLE : View.GONE);
                next.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void emailExistCheck() {
        // TODO: WEB send - JSON Receive
        usernameFromEmail(email.getText().toString());
    }

    // Use Regex for email format validation
    private void usernameFromEmail(String email) {
        if (email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            goNextEnable();
        } else {
            goNextDisable();
        }
    }

    @Deprecated
    private void goNextEnable() {
        runOnUiThread(() -> {
            next.setEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                next.setBackground(getDrawable(R.drawable.green_click));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                next.setBackground(getResources().getDrawable(R.drawable.green_click));
            } else {
                next.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_click));
            }
        });
    }

    @Deprecated
    private void goNextDisable() {
        runOnUiThread(() -> {
            next.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                next.setBackground(getDrawable(R.color.black_20));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                next.setBackground(getResources().getDrawable(R.color.black_20));
            } else {
                next.setBackgroundDrawable(getResources().getDrawable(R.color.black_20));
            }
            email.setError("이메일 형식이 잘못되었습니다.");
        });
    }

    @OnClick(R.id.prev)
    void prevPage() {
        finish();
        overridePendingTransition(0, R.animator.fade_out);
    }


    @OnClick(R.id.next)
    void nextPage() {
        SharedPreferenceHelper.setValue(this, AppConstant.EMAIL, email.getText().toString());
        startActivity(new Intent(getApplicationContext(), JoinPasswordActivity.class));
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSoftKeyboard != null) mSoftKeyboard.unRegisterSoftKeyboardCallback();
    }
}
