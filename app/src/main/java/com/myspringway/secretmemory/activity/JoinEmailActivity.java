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
import com.myspringway.secretmemory.constant.AppConstant;
import com.myspringway.secretmemory.helper.SharedPreferenceHelper;
import com.myspringway.secretmemory.library.ClearableEditText;
import com.myspringway.secretmemory.library.SoftKeyboard;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yoontaesup on 2016. 6. 11..
 */

public class JoinEmailActivity extends Activity {

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

        initObject();
    }

    void initObject() {
        email.setEmailType(true);

        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);

        mSoftKeyboard = new SoftKeyboard(root_layout, im);
        mSoftKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
                setVisibleBottom(true);
                EmailExistCheck();
            }

            @Override
            public void onSoftKeyboardShow() {
                setVisibleBottom(false);
            }
        });

    }

    public void setVisibleBottom(final boolean isVisible) {
        runOnUiThread(new Runnable() {
            public void run() {
                prev.setVisibility(isVisible ? View.VISIBLE : View.GONE);
                next.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSoftKeyboard != null) mSoftKeyboard.unRegisterSoftKeyboardCallback();
    }

    public void EmailExistCheck() {
        goNextEnable();
    }

    public void goNextEnable() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                next.setEnabled(true);
                if (Build.VERSION.SDK_INT >= 21) {
                    next.setBackground(getDrawable(R.drawable.green_click));
                } else {
                    next.setBackground(getResources().getDrawable(R.drawable.green_click));
                }
            }
        });
    }


    @OnClick(R.id.next)
    void nextPage() {
        SharedPreferenceHelper.setValue(this, AppConstant.EMAIL, email.getText().toString());
        Intent intent = new Intent(getApplicationContext(), JoinPasswordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }

    @OnClick(R.id.prev)
    void prevPage() {
        finish();
    }
}
