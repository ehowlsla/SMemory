package com.myspringway.secretmemory.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.fragment.MenuFragment;
import com.myspringway.secretmemory.slideMenu.SlidingFragmentActivity;
import com.myspringway.secretmemory.slideMenu.SlidingMenu;

/**
 * Created by yoontaesup on 16. 6. 15..
 */
public class BaseActivity extends SlidingFragmentActivity {

    private int mTitleRes;
    protected MenuFragment mFrag;

    public BaseActivity(int titleRes) {
        this.mTitleRes = titleRes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(mTitleRes);

        // set the Behind View
        setBehindContentView(R.layout.menu_frame);
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
            mFrag = new MenuFragment();
            fragmentTransaction.replace(R.id.menu_frame, mFrag);
            fragmentTransaction.commit();
        } else {
            mFrag = (MenuFragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        }

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
    }
}
