package icbc.com.musiccut.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import icbc.com.musiccut.R;
import icbc.com.musiccut.adapter.LocalMusicAdapter;
import icbc.com.musiccut.base.BaseFragment;
import icbc.com.musiccut.callback.LocalMusicCallBack;
import icbc.com.musiccut.model.LocalMusicEntity;
import icbc.com.musiccut.utils.ScanMusicUtils;

/**
 * Created By RedWolf on 2018/10/11 13:56
 * 乐库-合成
 */

public class SyntheticMusicFragment extends BaseFragment {
    private static SyntheticMusicFragment sSyntheticMusicFragment;

    public static SyntheticMusicFragment getInstance() {
        if (sSyntheticMusicFragment == null) {
            sSyntheticMusicFragment = new SyntheticMusicFragment();
        }
        return sSyntheticMusicFragment;
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<LocalMusicEntity> mLocalMusicList;
    private LocalMusicAdapter mLocalMusicAdapter;
    private LinearLayoutManager mManager;
    private TextView mTvDataEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_local_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        initEvent();
    }

    private void initData() {
        initMusicList();
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        mTvDataEmpty = view.findViewById(R.id.mTvDataEmpty);
        mSwipeRefreshLayout = view.findViewById(R.id.mSwipeRefreshLayout);
    }

    private void initMusicList() {
        //  读取本地音乐..
        //        mLocalMusicList = ScanMusicUtils.getMusicData(getActivity());
        //  TODO 模拟没有数据
        mLocalMusicList = new ArrayList<>();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (mLocalMusicList != null && mLocalMusicList.size() != 0) {
            Collections.sort(mLocalMusicList);
            mLocalMusicAdapter = new LocalMusicAdapter(mLocalMusicList, getActivity());
            mManager = new LinearLayoutManager(getActivity());
            mTvDataEmpty.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setLayoutManager(mManager);
            mRecyclerView.setAdapter(mLocalMusicAdapter);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mTvDataEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void initEvent() {
        if (mLocalMusicAdapter != null) {
            mLocalMusicAdapter.addLocalMusicCallBack(new LocalMusicCallBack() {
                @Override
                public void onItemClick(int pos) {
                    ToastUtils.showShort("onItemClick 点击的是" + mLocalMusicList.get(pos).getMusicName());
                }

                @Override
                public void onMenuClick(int pos) {
                    ToastUtils.showShort("onMenuClick 点击的是" + mLocalMusicList.get(pos).getMusicName());
                }
            });
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initMusicList();
            }
        });

    }

}
