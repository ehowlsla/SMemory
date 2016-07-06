package com.myspringway.secretmemory.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myspringway.secretmemory.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
    }
}
