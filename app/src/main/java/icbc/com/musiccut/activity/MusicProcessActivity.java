package icbc.com.musiccut.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import icbc.com.musiccut.R;
import icbc.com.musiccut.base.BaseActivity;
import icbc.com.musiccut.constants.Constants;
import icbc.com.musiccut.model.LocalMusicEntity;

public class MusicProcessActivity extends BaseActivity implements View.OnClickListener {
    public static void actionStart(Context context, String processName, LocalMusicEntity entity) {
        Intent intent = new Intent(context, MusicProcessActivity.class);
        intent.putExtra(Constants.EXTRA_KEY_PROCESS_MUSIC, entity);
        intent.putExtra(Constants.EXTRA_KEY_PROCESS_NAME, processName);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, String processName) {
        Intent intent = new Intent(context, MusicProcessActivity.class);
        intent.putExtra(Constants.EXTRA_KEY_PROCESS_NAME, processName);
        context.startActivity(intent);
    }

    private TextView mTvTitle, mTvExplain, mTvProcessMusicName;
    private ImageView mIvBack, mIvProcess, mIvProcessRight;
    private ConstraintLayout mChooseLayout;

    private LocalMusicEntity mLocalMusicEntity;
    private String mProcessName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_process);
        initParam();
        initView();
        initEvent();
    }


    private void initParam() {
        Intent intent = getIntent();
        mLocalMusicEntity = (LocalMusicEntity) intent.getSerializableExtra(Constants.EXTRA_KEY_PROCESS_MUSIC);
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
        //
        mTvTitle.setText(mProcessName);
        if (mLocalMusicEntity != null) {
            mTvProcessMusicName.setText(mLocalMusicEntity.getMusicEasyName());
        } else {
            mTvProcessMusicName.setText(getResources().getString(R.string.string_click_choose_music));
        }
    }


    private void initEvent() {
        bindClickByView(this, mTvExplain, mIvBack, mChooseLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTvExplain:
                break;
            case R.id.mIvBack:
                break;
            case R.id.mChooseLayout:
                SearchActivity.actionStartForResult(this, Constants.REQUEST_CODE_GET_MUSIC);
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
            case Constants.REQUEST_CODE_GET_MUSIC:
                mLocalMusicEntity = (LocalMusicEntity) data.getSerializableExtra(Constants.EXTRA_KEY_PROCESS_MUSIC);
                if (mLocalMusicEntity == null) {
                    return;
                }
                mTvProcessMusicName.setText(mLocalMusicEntity.getMusicEasyName());
                break;
        }
    }
}
