package icbc.com.musiccut.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import icbc.com.musiccut.R;
import icbc.com.musiccut.adapter.LocalMusicAdapter;
import icbc.com.musiccut.base.BaseActivity;
import icbc.com.musiccut.callback.LocalMusicCallBack;
import icbc.com.musiccut.callback.MusicPlayCallBack;
import icbc.com.musiccut.constants.Constants;
import icbc.com.musiccut.manager.MediaPlayManager;
import icbc.com.musiccut.model.LocalMusicEntity;
import icbc.com.musiccut.utils.MusicUtils;
import icbc.com.musiccut.utils.ScanMusicUtils;
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
    private Intent mIntent;
    //  标记是否从别的界面跳转入SearchActivity获取音乐的
    private boolean mIsForResult;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
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
        initEvent();
    }


    private void initParam() {
        mIntent = getIntent();
        mIsForResult = mIntent.getBooleanExtra(Constants.EXTRA_KEY_IS_FOR_RESULT, false);
    }

    private void initEvent() {
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bindClickByView(this, mBtnPath);

    }

    private void filter(String aa) {
        Pattern pattern = Pattern.compile(aa);
        //
        List<LocalMusicEntity> mList = new ArrayList<>();
        for (int i = 0; i < mLocalMusicList.size(); i++) {
            LocalMusicEntity entity = mLocalMusicList.get(i);
            String musicName = entity.getMusicEasyName();
            Matcher matcher = pattern.matcher(musicName);
            //
            if (matcher.find()) {
                mList.add(entity);
            }
        }
        mLocalMusicAdapter = new LocalMusicAdapter(mList, SearchActivity.this, true);
        mRecyclerView.setAdapter(mLocalMusicAdapter);
    }


    private void initView() {
        mTvBack = findViewById(R.id.mTvBack);
        mEtSearch = findViewById(R.id.mEtSearch);
        mBtnScan = findViewById(R.id.mBtnScan);
        mBtnPath = findViewById(R.id.mBtnPath);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this::initMusicList);

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
                mLocalMusicAdapter = new LocalMusicAdapter(mLocalMusicList, mActivity, true);
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
            case R.id.mBtnPath:
                showFileChooser();
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
        mHandler.post(() -> {
            mPlayDialog.setMusicProgress(progress);
            mPlayDialog.setSeekBarProgress(progress);
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        //  Dialog 消失事件
        //  关音乐
        mMediaPlayManager.stopMusic();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
//        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "XXX"), Constants.REQUEST_CODE_GET_MUSIC_PATH);
        } catch (android.content.ActivityNotFoundException ex) {
            //  TODO 直接通过Intent跳转到应用市场让他下载..
            ToastUtils.showShort("请安装文件管理器");
        }
    }

    @Override
    public void onItemClick(int pos) {
        //  否则 弹出音乐播放
        if (mPlayDialog != null &&
                mPlayDialog.isShowing()) {
            mPlayDialog.cancelDialog();
        } else {
            showDialog(mLocalMusicList.get(pos));
        }
    }

    @Override
    public void onMenuClick(int pos) {
        MenuDialog.build(this, mLocalMusicList.get(pos).getMusicPath()).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUEST_CODE_GET_MUSIC_PATH:
                Uri uri = data.getData();
                if (uri == null) {
                    return;
                }
                String path = MusicUtils.getPath(this, uri);
                if (mIsForResult) {
                    //  如果是ForResult  setResult finish
                    mIntent.putExtra(Constants.EXTRA_KEY_PROCESS_MUSIC_PATH, path);
                    setResult(RESULT_OK, mIntent);
                    finish();
                } else {
                    //  否则 展示Menu
                    MenuDialog.build(this, path).show();
                }
                break;
        }
    }


}
