package icbc.com.musiccut.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

import icbc.com.musiccut.R;

/**
 * Created By RedWolf on 2018/10/15 9:29
 * PlayDialog.java
 */


public class PlayDialog extends Dialog {
    private TextView mTvMusicProgress;
    private TextView mTvMusicMaxProgress;
    private TextView mTvMusicName;
    private SeekBar mSbMusicProcess;
    private ImageView mIvMusicPlay;

    private String mMaxProgress;
    private String mMusicName;
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener;
    private View.OnClickListener mOnClickListener;


    private PlayDialog(@NonNull Context context, String maxProgress, String musicName,
                       SeekBar.OnSeekBarChangeListener onSeekBarChangeListener, View.OnClickListener onClickListener) {
        super(context, R.style.PlayDialog);
        this.mMaxProgress = maxProgress;
        this.mMusicName = musicName;
        this.mOnSeekBarChangeListener = onSeekBarChangeListener;
        this.mOnClickListener = onClickListener;
    }

    private static PlayDialog playDialog;

    public static PlayDialog build(Context context, String maxProgress, String musicName,
                                   SeekBar.OnSeekBarChangeListener onSeekBarChangeListener, View.OnClickListener onClickListener) {
        if (playDialog != null) {
            playDialog.cancelDialog();
        }
        playDialog = new PlayDialog(context, maxProgress, musicName, onSeekBarChangeListener, onClickListener);
        return playDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_play_layout);
        mTvMusicProgress = findViewById(R.id.mTvMusicProgress);
        mTvMusicMaxProgress = findViewById(R.id.mTvMusicMaxProgress);
        mTvMusicName = findViewById(R.id.mTvMusicName);
        mSbMusicProcess = findViewById(R.id.mSbMusicProcess);
        mIvMusicPlay = findViewById(R.id.mIvMusicPlay);
        //
        mTvMusicProgress.setText(R.string.string_play_start_time);
        mTvMusicName.setText(mMusicName);
        mTvMusicMaxProgress.setText(mMaxProgress);
        mSbMusicProcess.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        mIvMusicPlay.setOnClickListener(mOnClickListener);

    }

    public void setMusicProgress(String progress) {
        if (mTvMusicProgress != null) {
            mTvMusicMaxProgress.setText(progress);
        }
    }

    public void setSeekBarProgress(int progress) {
        if (mSbMusicProcess != null) {
            mSbMusicProcess.setProgress(progress);
        }
    }

    public void cancelDialog() {
        mTvMusicProgress = null;
        mTvMusicMaxProgress = null;
        mTvMusicName = null;
        mSbMusicProcess = null;
        mIvMusicPlay = null;
        playDialog = null;
        this.dismiss();
        cancel();
    }

    @Override
    public void show() {
        super.show();
        /*
         * 设置屏幕宽度
         */
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().getDecorView().setPadding(0, 0, 0, 0);
            getWindow().setAttributes(layoutParams);
        }
    }
}
