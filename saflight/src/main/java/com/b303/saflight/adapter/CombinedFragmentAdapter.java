package com.b303.saflight.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by BaiChangCai on 2017/7/1.
 * fragment适配器
 */
public class CombinedFragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> mList;

    public CombinedFragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        if(mList!=null)
            return mList.get(position);
        return null;
    }

    @Override
    public int getCount() {
        if(mList!=null)
            return mList.size();
        return 0;
    }
}
