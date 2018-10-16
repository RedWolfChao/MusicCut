package icbc.com.musiccut.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import icbc.com.musiccut.R;
import icbc.com.musiccut.adapter.MainViewPagerAdapter;
import icbc.com.musiccut.base.BaseFragment;

/**
 * Created By RedWolf on 2018/10/11 13:57
 * 乐库
 */

public class MainMusicFragment extends BaseFragment {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MainViewPagerAdapter mMainViewPagerAdapter;
    private List<BaseFragment> mBaseFragmentList;

    private static MainMusicFragment sMainMusicFragment;

    public static MainMusicFragment getInstance() {
        if (sMainMusicFragment == null) {
            sMainMusicFragment = new MainMusicFragment();
        }
        return sMainMusicFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
        initEvent();
    }


    private void initData() {
        mBaseFragmentList = new ArrayList<>();
        mBaseFragmentList.add(LocalMusicFragment.getInstance());
        mBaseFragmentList.add(CutMusicFragment.getInstance());
        mBaseFragmentList.add(SyntheticMusicFragment.getInstance());
        mBaseFragmentList.add(TranslateMusicFragment.getInstance());
        mBaseFragmentList.add(OtherMusicFragment.getInstance());
        mMainViewPagerAdapter = new MainViewPagerAdapter(getFragmentManager(), mBaseFragmentList);
    }

    private void initView(View view) {
        mViewPager = view.findViewById(R.id.mFragMainViewPager);
        mTabLayout = view.findViewById(R.id.mFragMainTabLayout);

        //
        mViewPager.setAdapter(mMainViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initEvent() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
