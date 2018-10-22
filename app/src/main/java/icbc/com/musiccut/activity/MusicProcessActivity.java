package icbc.com.musiccut.activity;

import android.content.Context;
import android.content.Intent;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.IOException;

import icbc.com.musiccut.R;
import icbc.com.musiccut.base.BaseActivity;
import icbc.com.musiccut.callback.MusicPlayCallBack;
import icbc.com.musiccut.constants.Constants;
import icbc.com.musiccut.manager.MediaPlayManager;
import icbc.com.musiccut.utils.MusicUtils;
import icbc.com.musiccut.view.SpectrumView;

public class MusicProcessActivity extends BaseActivity implements View.OnClickListener, MusicPlayCallBack {
    private MediaPlayManager mMediaPlayManager;
    private Visualizer mVisualizer;

    public static void actionStart(Context context, String processName, String musicPath) {
        Intent intent = new Intent(context, MusicProcessActivity.class);
        intent.putExtra(Constants.EXTRA_KEY_PROCESS_MUSIC_PATH, musicPath);
        intent.putExtra(Constants.EXTRA_KEY_PROCESS_NAME, processName);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, String processName) {
        Intent intent = new Intent(context, MusicProcessActivity.class);
        intent.putExtra(Constants.EXTRA_KEY_PROCESS_NAME, processName);
        context.startActivity(intent);
    }

    private TextView mTvTitle, mTvExplain, mTvProcessMusicName;
    private ImageView mIvBack, mIvProcess, mIvProcessRight, mIvCutPlay;
    private ConstraintLayout mChooseLayout;
    private SpectrumView mSpectrumView;

    private String mMusicPath;
    private String mProcessName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_process);
        initParam();
        initData();
        initView();
        initEvent();
    }

    private void initData() {
        mMediaPlayManager = new MediaPlayManager();
    }


    private void initParam() {
        Intent intent = getIntent();
        mMusicPath = intent.getStringExtra(Constants.EXTRA_KEY_PROCESS_MUSIC_PATH);
        mProcessName = intent.getStringExtra(Constants.EXTRA_KEY_PROCESS_NAME);
    }

    private void initView() {
        mTvTitle = findViewById(R.id.mTvTitle);
        mTvExplain = findViewById(R.id.mTvExplain);
        mTvProcessMusicName = findViewById(R.id.mTvProcessMusicName);
        mIvProcessRight = findViewById(R.id.mIvProcessRight);
        mIvBack = findViewById(R.id.mIvBack);
        mIvProcess = findViewById(R.id.mIvProcess);
        mChooseLayout = findViewById(R.id.mChooseLayout);
        mIvCutPlay = findViewById(R.id.mIvCutPlay);
        //
        mTvTitle.setText(mProcessName);
        if (!StringUtils.isEmpty(mMusicPath)) {
            startVisualiser();
            mTvProcessMusicName.setText(MusicUtils.getMusicNameByPath(mMusicPath));
        } else {
            mTvProcessMusicName.setText(getResources().getString(R.string.string_click_choose_music));
        }
    }


    private void initEvent() {
        bindClickByView(this, mTvExplain, mIvBack, mChooseLayout, mIvCutPlay);
    }

    /**
     * 获取扩音器中的声音信息
     */
    private void startVisualiser() {
        if (mVisualizer != null) {
            mVisualizer.setEnabled(false);
            mVisualizer.release();
        }
        mVisualizer = new Visualizer(0); // 初始化
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                if (mMediaPlayManager != null && mMediaPlayManager.isPlaying()) {
                    mSpectrumView.startDraw(waveform);
                }
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

            }
        }, Visualizer.getMaxCaptureRate(), true, false);
        mVisualizer.setCaptureSize(Constants.CAPTURE_SIZE);
        mVisualizer.setEnabled(true);
    }

    @Override
    protected void onPause() {
        if (mVisualizer != null) {
            mVisualizer.release();
        }
        if (mMediaPlayManager != null && mMediaPlayManager.isPlaying()) {
            mMediaPlayManager.stopMusic();
            mMediaPlayManager = null;
        }
        if (mSpectrumView != null) {
            mSpectrumView.stopDraw();
            mSpectrumView = null;
        }
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSpectrumView = findViewById(R.id.mSpectrumView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTvExplain:
                break;
            case R.id.mIvBack:
                break;
            case R.id.mIvCutPlay:
                if (StringUtils.isEmpty(mMusicPath)) {
                    ToastUtils.showShort("请先选择音乐");
                    return;
                }
                //  播放音乐
                try {
                    mMediaPlayManager.init(mMusicPath, this);
                    mMediaPlayManager.playMusic();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.mChooseLayout:
                SearchActivity.actionStartForResult(this, Constants.REQUEST_CODE_GET_MUSIC_PATH);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUEST_CODE_GET_MUSIC_PATH:
                mMusicPath = data.getStringExtra(Constants.EXTRA_KEY_PROCESS_MUSIC_PATH);
                if (StringUtils.isEmpty(mMusicPath)) {
                    return;
                }
                startVisualiser();
                mTvProcessMusicName.setText(MusicUtils.getMusicNameByPath(mMusicPath));
                break;
        }
    }

    @Override
    public void onMusicProgress(int progress) {

    }

}
