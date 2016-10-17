package com.myspringway.secretmemory.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.library.ClearableEditText;
import com.myspringway.secretmemory.library.SoftKeyboard;
import com.myspringway.secretmemory.memory.CurrentChurch;
import com.myspringway.secretmemory.model.thirdparty.Pastor;
import com.myspringway.secretmemory.model.thirdparty.ResPastor;
import com.myspringway.secretmemory.presenter.JoinChurchPresenter;
import com.myspringway.secretmemory.service.RestService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yoontaesup on 2016. 10. 16..
 */

public class JoinChurchActivity extends Activity {

    @BindView(R.id.pastor)
    ClearableEditText pastor;

    @BindView(R.id.prev)
    TextView prev;

    @BindView(R.id.next)
    TextView next;

    @BindView(R.id.root_layout)
    RelativeLayout root_layout;

    JoinChurchPresenter presenter;

    private SoftKeyboard mSoftKeyboard;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_join_church);
        ButterKnife.bind(this);
        context = this;

        presenter = new JoinChurchPresenter(this, new RestService(getApplicationContext()));
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
            Boolean enable = pastor.getText().length() > 0;

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
        if (pastor.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.join_pastor_info, Toast.LENGTH_SHORT).show();
            return;
        }
        presenter.getPastor(pastor.getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSoftKeyboard != null) mSoftKeyboard.unRegisterSoftKeyboardCallback();
    }

    void popupPastor(List<Pastor> items) {
        if(items == null || items.size() == 0) {
            goNameActivity();
            return;
        } else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setTitle(R.string.name_select);

            // List Adapter 생성
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_singlechoice);
            for(Pastor item : items) {
                adapter.add(item.churchName + " " + item.pastor);
//                adapter.add(item.churchName + " " + item.name + "(" + item.birthDay + ")");
            }
            adapter.add(getString(R.string.name_no_exist));

            // 버튼 생성
            alertBuilder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

            // Adapter 셋팅
            alertBuilder.setAdapter(adapter,
                    (dialog, id) -> {
                        if(id < items.size()) savePastorInfo(items.get(id));
                        goNameActivity();
                    });
            alertBuilder.show();
        }
    }

    void savePastorInfo(Pastor pastor) {
        CurrentChurch.getInstance().pastor = pastor;
    }

    void goNameActivity() {
        startActivity(new Intent(getApplicationContext(), JoinNameActivity.class));
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }

    public void callbackSuccess(ResPastor resPastor) {
        if (resPastor.error == 0) {
            popupPastor(resPastor.items);
        } else {
            Toast.makeText(getApplicationContext(), resPastor.message, Toast.LENGTH_SHORT).show();
        }
    }

}
