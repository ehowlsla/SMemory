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
import android.widget.Toast;

import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.constants.AppConstant;
import com.myspringway.secretmemory.helper.SharedPreferenceHelper;
import com.myspringway.secretmemory.library.ClearableEditText;
import com.myspringway.secretmemory.library.SoftKeyboard;
import com.myspringway.secretmemory.model.thirdparty.ResName;
import com.myspringway.secretmemory.model.thirdparty.ResPastor;
import com.myspringway.secretmemory.presenter.JoinChurchPresenter;
import com.myspringway.secretmemory.presenter.JoinNamePresenter;
import com.myspringway.secretmemory.service.RestService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yoontaesup on 2016. 10. 16..
 */

public class JoinNameActivity extends Activity {
    @BindView(R.id.name)
    ClearableEditText name;

    @BindView(R.id.prev)
    TextView prev;

    @BindView(R.id.next)
    TextView next;

    @BindView(R.id.root_layout)
    RelativeLayout root_layout;

    JoinNamePresenter presenter;

    private SoftKeyboard mSoftKeyboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_name);
        ButterKnife.bind(this);

        presenter = new JoinNamePresenter(this, new RestService(getApplicationContext()));
    }


    @Override
    protected void onResume() {
        super.onResume();
        initObject();
    }

    private void initObject() {
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);

        mSoftKeyboard = new SoftKeyboard(root_layout, im);
        mSoftKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
                setVisibleBottom(true);
                updateNextEnable();
            }

            @Override
            public void onSoftKeyboardShow() {
                setVisibleBottom(false);
            }
        });
    }

    void updateNextEnable() {
        runOnUiThread(() -> {
            Boolean enable = name.getText().length() > 0;

            next.setEnabled(enable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                next.setBackground(getDrawable(enable == true ? R.drawable.green_click : R.color.black_20));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                next.setBackground(getResources().getDrawable(enable == true ? R.drawable.green_click : R.color.black_20));
            } else {
                next.setBackgroundDrawable(getResources().getDrawable(enable == true ? R.drawable.green_click : R.color.black_20));
            }
        });
    }
    private void setVisibleBottom(final boolean isVisible) {
        runOnUiThread(() -> {
            prev.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            next.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        });
    }

    @OnClick(R.id.prev)
    void prevPage() {
        finish();
        overridePendingTransition(0, R.animator.fade_out);
    }


    @OnClick(R.id.next)
    void nextPage() {
        if (name.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.join_name_info, Toast.LENGTH_SHORT).show();
            return;
        }
        presenter.getName(name.getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSoftKeyboard != null) mSoftKeyboard.unRegisterSoftKeyboardCallback();
    }



    public void callbackSuccess(ResName resName) {
        if (resName.error == 0) {
            SharedPreferenceHelper.setValue(this, AppConstant.NAME, name.getText().toString());
            startActivity(new Intent(getApplicationContext(), JoinEmailActivity.class));
            overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        } else {
            Toast.makeText(getApplicationContext(), resName.message, Toast.LENGTH_SHORT).show();
        }
    }
}