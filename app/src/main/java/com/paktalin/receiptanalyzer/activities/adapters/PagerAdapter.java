package com.paktalin.receiptanalyzer.activities.adapters;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.paktalin.receiptanalyzer.activities.OverviewFragment;
import com.paktalin.receiptanalyzer.activities.Tab1;
import com.paktalin.receiptanalyzer.activities.AllReceiptsFragment;

/**
 * Created by Paktalin on 05/05/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private int mNoOfTabs;

    PagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {

            case 0:
                return new Tab1();
            case 1:
                return  new OverviewFragment();
            case 2:
                return new AllReceiptsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
