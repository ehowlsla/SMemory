package com.myspringway.secretmemory.activity;

import android.os.Bundle;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.fragment.CardFragment;
import com.myspringway.secretmemory.fragment.adapter.SwipeDeckAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity{

    private static final String TAG = "MainActivity";

    private CardFragment fragment;

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


