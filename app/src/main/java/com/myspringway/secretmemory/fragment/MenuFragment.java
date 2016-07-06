package com.myspringway.secretmemory.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.activity.MainActivity;
import com.myspringway.secretmemory.activity.SignActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yoontaesup on 2015. 5. 7..
 */
public class MenuFragment extends Fragment {

    FirebaseAuth mAuth;

    @BindView(R.id.row1_layout)
    LinearLayout row1_layout;

    @BindView(R.id.row2_layout)
    LinearLayout row2_layout;

    @BindView(R.id.row3_layout)
    LinearLayout row3_layout;

    @BindView(R.id.row4_layout)
    LinearLayout row4_layout;

    @BindView(R.id.signOut)
    TextView signOut;

    @OnClick(R.id.signOut)
    void onSignOutClick() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        mAuth.signOut();
    }

    @OnClick(R.id.row1_layout)
    void onClickRow1() {
        onClickRow(1);
    }

    @OnClick(R.id.row2_layout)
    void onClickRow2() {
        onClickRow(2);
    }

    @OnClick(R.id.row3_layout)
    void onClickRow3() {
        onClickRow(3);
    }

    @OnClick(R.id.row4_layout)
    void onClickRow4() {
        onClickRow(4);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    void onClickRow(int row) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        ft.replace(R.id.framelayout, getFragment(row)).commit();

        ((MainActivity)getActivity()).showContent();
    }

    private CardFragment cardFragment;
    private MyCardFragment myCardFragment;

    Fragment getFragment(int row) {
        if(row == 1) {
            if(cardFragment == null) cardFragment = new CardFragment();
            return cardFragment;
        } else if(row == 2) {
            if(myCardFragment == null) myCardFragment = new MyCardFragment();
            return myCardFragment;
        }
        else return new Fragment();
    }

//    private View parentView;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        parentView = inflater.inflate(R.layout.fragment_menu, container, false);
//        setUpViews();
//        return parentView;
//    }


    private void setUpViews() {
//        MainActivity_ parentActivity = (MainActivity_) getActivity();
//        resideMenu = parentActivity.getResideMenu();

//        parentView.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
//                if(parent != null)
//                    ((MainActivity) parent).toggle();
//            }
//        });
//
//        // add gesture operation's ignored views
//        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.pager);
//        resideMenu.addIgnoredView(ignored_view);
//        resideMenu.g
//        RelativeLayout root_view = (RelativeLayout) parentView.findViewById(R.id.root_view);
//        resideMenu.addIgnoredView(root_view);
    }
}
