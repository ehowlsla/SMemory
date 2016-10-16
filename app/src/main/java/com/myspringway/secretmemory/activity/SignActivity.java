package com.myspringway.secretmemory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.constants.AppConstant;
import com.myspringway.secretmemory.helper.SharedPreferenceHelper;
import com.myspringway.secretmemory.sign.CircleIndicator;
import com.myspringway.secretmemory.sign.ParallaxPagerTransformer;
import com.myspringway.secretmemory.sign.SignParallaxAdapter;
import com.myspringway.secretmemory.sign.SignParallaxFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by yoontaesup on 2016. 6. 11..
 */

public class SignActivity extends FragmentActivity {

    @BindView(R.id.pager)
    ViewPager mPager;
    SignParallaxAdapter mAdapter;

    @BindView(R.id.next)
    TextView next;

    @BindView(R.id.indicator_default)
    CircleIndicator indicator_default;

    private boolean isTwoClickBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_parallax);
        ButterKnife.bind(this);

        initObject();
        initActivity();
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


    void initActivity() {
//        registGCM();
        SharedPreferenceHelper.setValue(this, AppConstant.UUID, Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
    }


    void initObject() {
        mPager.setBackgroundColor(0xFF000000);

        ParallaxPagerTransformer pt = new ParallaxPagerTransformer((R.id.image));

        mPager.setPageTransformer(false, pt);

        mAdapter = new SignParallaxAdapter(getSupportFragmentManager());
        mAdapter.setPager(mPager); //only for this transformer

        for (int i = 0; i < 6; i++) {
            Bundle args = new Bundle();
            args.putInt("position", i);
            SignParallaxFragment fragment = new SignParallaxFragment();
            fragment.setArguments(args);
            fragment.setParent(this);
            mAdapter.add(fragment);
        }

        mPager.setAdapter(mAdapter);

        // DEFAULT
//        CircleIndicator defaultIndicator = (CircleIndicator) findViewById(R.id.indicator_default);
        indicator_default.setViewPager(mPager);
        indicator_default.setParent(this);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().show();
        }

//        PopupDialog dialog = new PopupDialog();
//        dialog.show(getFragmentManager(), "progress");
    }

    public void changeItem(int position) {
        next.setVisibility(position == mAdapter.getCount() - 1 ? View.GONE : View.VISIBLE);
    }

    public void goJoinActivity() {
        Intent intent = new Intent(getApplicationContext(), JoinChurchActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        finish();
    }

    public void goLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        finish();
    }

    @OnClick(R.id.next)
    void nextPage() {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}
