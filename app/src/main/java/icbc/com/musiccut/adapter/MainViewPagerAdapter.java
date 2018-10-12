package icbc.com.musiccut.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import icbc.com.musiccut.base.BaseFragment;

/**
 * Created By RedWolf on 2018/10/12 9:04
 * MainViewPagerAdapter.java
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> mBaseFragmentList;

    public MainViewPagerAdapter(FragmentManager fm, List<BaseFragment> baseFragmentList) {
        super(fm);
        this.mBaseFragmentList = baseFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mBaseFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mBaseFragmentList.size();
    }
}
