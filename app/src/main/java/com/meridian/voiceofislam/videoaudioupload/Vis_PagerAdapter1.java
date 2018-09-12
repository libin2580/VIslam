package com.meridian.voiceofislam.videoaudioupload;

import android.support.v4.app.FragmentManager;

/**
 * Created by Libin_Cybraum on 8/19/2016.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
/**
 * Created by Libin_Cybraum on 7/30/2016.
 */

public class Vis_PagerAdapter1 extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public Vis_PagerAdapter1(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Vis_TabFragment1 tab1 = new Vis_TabFragment1();
                return tab1;
            case 1:
                Vis_TabFragment2 tab2 = new Vis_TabFragment2();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}