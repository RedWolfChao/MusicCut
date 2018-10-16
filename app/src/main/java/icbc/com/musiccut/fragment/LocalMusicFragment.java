package icbc.com.musiccut.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import icbc.com.musiccut.R;
import icbc.com.musiccut.adapter.LocalMusicAdapter;
import icbc.com.musiccut.base.BaseFragment;
import icbc.com.musiccut.callback.LocalMusicCallBack;
import icbc.com.musiccut.callback.MusicPlayCallBack;
import icbc.com.musiccut.model.EventShowMenu;
import icbc.com.musiccut.model.LocalMusicEntity;
import icbc.com.musiccut.utils.ScanMusicUtils;
import icbc.com.musiccut.utils.manager.MediaPlayManager;
import icbc.com.musiccut.view.PlayDialog;

/**
 * Created By RedWolf on 2018/10/11 13:56
 * 乐库-本地
 */

public class LocalMusicFragment extends BaseFragment implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, MusicPlayCallBack,
        DialogInterface.OnDismissListener, LocalMusicCallBack {
    private static LocalMusicFragment sLocalMusicFragment;

    public static LocalMusicFragment getInstance() {
        if (sLocalMusicFragment == null) {
            sLocalMusicFragment = new LocalMusicFragment();
        }
        return sLocalMusicFragment;
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<LocalMusicEntity> mLocalMusicList;
    private LocalMusicAdapter mLocalMusicAdapter;
    private LinearLayoutManager mManager;
    private TextView mTvDataEmpty;
    private PlayDialog mPlayDialog;
    private MediaPlayManager mMediaPlayManager;
    private boolean mIsInit;
    private final Handler mHandler = new Handler();

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

    @SuppressLint("HandlerLeak")
    private void initData() {
        mMediaPlayManager = new MediaPlayManager();
        initMusicList();
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        mTvDataEmpty = view.findViewById(R.id.mTvDataEmpty);
        mSwipeRefreshLayout = view.findViewById(R.id.mSwipeRefreshLayout);
    }

    private void initMusicList() {
        mIsInit = true;
        //  读取本地音乐..
        if (mLocalMusicList == null) {
            mLocalMusicList = ScanMusicUtils.getMusicData(getActivity());
        } else {
            mLocalMusicList.clear();
            mLocalMusicList.addAll(ScanMusicUtils.getMusicData(getActivity()));
        }
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (mLocalMusicList != null && mLocalMusicList.size() != 0) {
            Collections.sort(mLocalMusicList);
            if (mManager == null) {
                mManager = new LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(mManager);
            }
            if (mLocalMusicAdapter == null) {
                mLocalMusicAdapter = new LocalMusicAdapter(mLocalMusicList, getActivity());
                mRecyclerView.setAdapter(mLocalMusicAdapter);
            } else {
                mLocalMusicAdapter.setLocalMusicEntityList(mLocalMusicList);
            }
            mLocalMusicAdapter.addLocalMusicCallBack(this);
            mTvDataEmpty.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mTvDataEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initMusicList();
            }
        });

    }

    private void showDialog(LocalMusicEntity entity) {
        //  初始化Dialog
        mPlayDialog = PlayDialog.build(getActivity(),
                entity.getMusicLong(),
                entity.getMusicPath(),
                this, this, this);
        //  弹出
        mPlayDialog.show();
        //  播放音乐
        try {
            mMediaPlayManager.init(entity.getMusicPath(), this);
            mMediaPlayManager.playMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        //
        switch (v.getId()) {
            case R.id.mIvMusicPlay:
                //  播放||暂停音乐
                mMediaPlayManager.playMusic();
                //
                if (mMediaPlayManager.isPlaying()) {
                    mPlayDialog.pausePlay();
                } else {
                    mPlayDialog.startPlay();
                }
                break;
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //  如果是用户滑动的才设置播放进度
        if (fromUser) {
            mMediaPlayManager.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //  TODO nothing ...

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //  TODO nothing ...
    }

    @Override
    public void onMusicProgress(final int progress) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mPlayDialog.setMusicProgress(progress);
                mPlayDialog.setSeekBarProgress(progress);
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        //  Dialog 消失事件
        //  关音乐
        mMediaPlayManager.stopMusic();
    }

    @Override
    public void onItemClick(int pos) {
        if (mPlayDialog != null &&
                mPlayDialog.isShowing()) {
            mPlayDialog.cancelDialog();
        } else {
            showDialog(mLocalMusicList.get(pos));
        }
    }

    @Override
    public void onMenuClick(int pos) {
        EventBus.getDefault().post(new EventShowMenu());
    }
}
