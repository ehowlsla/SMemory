package com.myspringway.secretmemory.fragment;

import android.support.v4.app.Fragment;

import com.myspringway.secretmemory.dialog.PopupLoading;


/**
 * Created by yoontaesup on 2015. 5. 16..
 */
public class BaseFragment extends Fragment {
    PopupLoading popupLoading;
    protected void startLoading() {
        popupLoading = new PopupLoading();
        popupLoading.show(getActivity().getFragmentManager(), "loading");
    }

    protected void stopLoading() {
        if(popupLoading != null) popupLoading.dismiss();
    }
}
