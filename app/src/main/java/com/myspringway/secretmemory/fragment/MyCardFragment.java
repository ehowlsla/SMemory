package com.myspringway.secretmemory.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myspringway.secretmemory.R;

import butterknife.ButterKnife;

/**
 * Created by ehowlsla on 2016. 6. 16..
 */
public class MyCardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_card, container, false);

        ButterKnife.bind(this, view);

        return view;
    }
}
