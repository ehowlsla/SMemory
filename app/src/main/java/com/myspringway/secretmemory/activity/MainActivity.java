package com.myspringway.secretmemory.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.fragment.CardFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private CardFragment fragment;


    private boolean isTwoClickBack = false;

    public MainActivity() {
        super(R.string.title_bar_slide);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fragment = new CardFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout, fragment)
                .commit();
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

    @OnClick(R.id.menu)
    void goMenuClick() {
        toggle();
        fragment.resetCardPosition();
    }

//    @Override
//    public void getSlidingMenu() {
//        super.getSlidingMenu();
//        fragment.resetCardPosition();
//    }
}


