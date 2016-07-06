package com.myspringway.secretmemory.sign;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;


/**
 * Created by yoontaesup on 2015. 4. 25..
 */
public class SignParallaxAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> mFragments;
    private ViewPager mPager;

    public SignParallaxAdapter(FragmentManager fm) {
        super(fm);


        mFragments = new ArrayList<>();
    }



    @Override
    public Fragment getItem(int i) {
//        Log.d("position : ", String.valueOf(i));
//        changeItem(i);
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void add(SignParallaxFragment parallaxFragment) {
        parallaxFragment.setAdapter(this);
        mFragments.add(parallaxFragment);
        notifyDataSetChanged();
        mPager.setCurrentItem(getCount() - 1, true);

    }

    public void remove(int i) {
        mFragments.remove(i);
        notifyDataSetChanged();
    }

    public void remove(SignParallaxFragment parallaxFragment) {
        mFragments.remove(parallaxFragment);

        int pos = mPager.getCurrentItem();
        notifyDataSetChanged();

        mPager.setAdapter(this);
        if (pos >= this.getCount()) {
            pos = this.getCount() - 1;
        }
        mPager.setCurrentItem(pos, true);

    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setPager(ViewPager pager) {
        mPager = pager;
    }

    public ViewPager getPager() {
        return mPager;
    }
}
