package com.myspringway.secretmemory.dialog;

import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.myspringway.secretmemory.R;
import com.pnikosis.materialishprogress.ProgressWheel;


/**
 * Created by yoontaesup on 2015. 5. 5..
 */
public class PopupLoading extends DialogFragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        View rootView = inflater.inflate(R.layout.popup_loading, container);
        ProgressWheel progress_wheel = (ProgressWheel) rootView.findViewById(R.id.progress_wheel);
        progress_wheel.setBarColor(getResources().getColor(R.color.green_on));
        progress_wheel.setBackground(getResources().getDrawable(R.color.transparent));
        progress_wheel.setRimColor(getResources().getColor(R.color.transparent));


        return rootView;
    }
}
