package icbc.com.musiccut.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import icbc.com.musiccut.R;
import icbc.com.musiccut.adapter.LocalMusicAdapter;
import icbc.com.musiccut.base.BaseActivity;
import icbc.com.musiccut.callback.LocalMusicCallBack;
import icbc.com.musiccut.callback.MusicPlayCallBack;
import icbc.com.musiccut.constants.Constants;
import icbc.com.musiccut.model.LocalMusicEntity;
import icbc.com.musiccut.utils.ScanMusicUtils;
import icbc.com.musiccut.utils.manager.MediaPlayManager;
import icbc.com.musiccut.view.MenuDialog;
import icbc.com.musiccut.view.PlayDialog;

public class SearchActivity extends BaseActivity implements LocalMusicCallBack, DialogInterface.OnDismissListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener, MusicPlayCallBack {
    private TextView mTvBack;
    private EditText mEtSearch;
    private Button mBtnScan, mBtnPath;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<LocalMusicEntity> mLocalMusicList;
    private LinearLayoutManager mManager;
    private LocalMusicAdapter mLocalMusicAdapter;
    private PlayDialog mPlayDialog;
    private MediaPlayManager mMediaPlayManager;
    private Handler mHandler = new Handler();
    //
    private boolean mIsForResult;
    private Intent mIntent;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(Constants.EXTRA_KEY_IS_FOR_RESULT, false);
        context.startActivity(intent);
    }

    public static void actionStartForResult(BaseActivity baseActivity, int requestCode) {
        Intent intent = new Intent(baseActivity, SearchActivity.class);
        intent.putExtra(Constants.EXTRA_KEY_IS_FOR_RESULT, true);
        baseActivity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initParam();
        initView();
        initData();
    }

    private void initParam() {
        mIntent = getIntent();
        mIsForResult = mIntent.getBooleanExtra(Constants.EXTRA_KEY_IS_FOR_RESULT, false);
    }

    private void initView() {
        mTvBack = findViewById(R.id.mTvBack);
        mEtSearch = findViewById(R.id.mEtSearch);
        mBtnScan = findViewById(R.id.mBtnScan);
        mBtnPath = findViewById(R.id.mBtnPath);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initMusicList();
            }
        });

    }

    private void initData() {
        mMediaPlayManager = new MediaPlayManager();
        initMusicList();
    }

    private void initMusicList() {
        //  读取本地音乐..
        if (mLocalMusicList == null) {
            mLocalMusicList = ScanMusicUtils.getMusicData(mActivity);
        } else {
            mLocalMusicList.clear();
            mLocalMusicList.addAll(ScanMusicUtils.getMusicData(mActivity));
        }
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (mLocalMusicList != null && mLocalMusicList.size() != 0) {
            Collections.sort(mLocalMusicList);
            if (mManager == null) {
                mManager = new LinearLayoutManager(mActivity);
                mRecyclerView.setLayoutManager(mManager);
            }
            if (mLocalMusicAdapter == null) {
                mLocalMusicAdapter = new LocalMusicAdapter(mLocalMusicList, mActivity, true, mIsForResult);
                mRecyclerView.setAdapter(mLocalMusicAdapter);
            } else {
                mLocalMusicAdapter.setLocalMusicEntityList(mLocalMusicList);
            }
            mLocalMusicAdapter.addLocalMusicCallBack(this);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showDialog(LocalMusicEntity entity) {
        //  初始化Dialog
        mPlayDialog = PlayDialog.build(mActivity,
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
            case R.id.mIvItemMusicPlay:
                //  Item
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
        if (mIsForResult) {
            //  如果来自ForResult 则 setResult
            mIntent.putExtra(Constants.EXTRA_KEY_PROCESS_MUSIC, mLocalMusicList.get(pos));
            setResult(RESULT_OK, mIntent);
            finish();
        } else {
            //  否则 弹出音乐播放
            if (mPlayDialog != null &&
                    mPlayDialog.isShowing()) {
                mPlayDialog.cancelDialog();
            } else {
                showDialog(mLocalMusicList.get(pos));
            }
        }
    }

    @Override
    public void onMenuClick(int pos) {
        MenuDialog.build(this, mLocalMusicList.get(pos)).show();
    }
}
