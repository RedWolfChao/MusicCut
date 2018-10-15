package icbc.com.musiccut.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.TextView;

import icbc.com.musiccut.R;
import icbc.com.musiccut.base.BaseActivity;
import icbc.com.musiccut.fragment.MainMusicFragment;
import icbc.com.musiccut.fragment.MainSettingFragment;

/**
 * Created By RedWolf on 2018/10/12 10:11
 * MainActivity.java
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private AppCompatEditText mEtSearch;
    private TextView mFootTvMusic;
    private TextView mFootTvSetting;
    private FloatingActionButton mFabAdd;
    private MainMusicFragment mMainMusicFragment;
    private MainSettingFragment mMainSettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initEvent();
    }

    private void initData() {
    }

    private void initView() {
        mEtSearch = findViewById(R.id.mEtSearch);
        mFootTvMusic = findViewById(R.id.mFootTvMusic);
        mFootTvSetting = findViewById(R.id.mFootTvSetting);
        mFabAdd = findViewById(R.id.mFabAdd);
        mEtSearch = findViewById(R.id.mEtSearch);
        //
    }

    private void initEvent() {
        bindClickByView(this, mFootTvMusic, mFootTvSetting, mFabAdd);
        mEtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //  TODO Search ..
            }
        });
        //  点击Music
        mFootTvMusic.performClick();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (v.getId()) {
            case R.id.mFootTvMusic:
                resetTab();
                mFootTvMusic.setTextColor(getResources().getColor(R.color.colorPrimary));
                Drawable musicDraw = getResources().getDrawable(R.mipmap.icon_music_selected);
                musicDraw.setBounds(0, 0, musicDraw.getMinimumWidth(), musicDraw.getMinimumHeight());
                mFootTvMusic.setCompoundDrawables(null, musicDraw, null, null);
                if (mMainMusicFragment == null) {
                    mMainMusicFragment = MainMusicFragment.getInstance();
                    transaction.add(R.id.mFrameMain, mMainMusicFragment);
                } else {
                    transaction.show(mMainMusicFragment);
                }
                transaction.commit();
                break;
            case R.id.mFootTvSetting:
                resetTab();
                mFootTvSetting.setTextColor(getResources().getColor(R.color.colorPrimary));
                Drawable settingDraw = getResources().getDrawable(R.mipmap.icon_setting_selected);
                settingDraw.setBounds(0, 0, settingDraw.getMinimumWidth(), settingDraw.getMinimumHeight());
                mFootTvSetting.setCompoundDrawables(null, settingDraw, null, null);
                if (mMainSettingFragment == null) {
                    mMainSettingFragment = MainSettingFragment.getInstance();
                    transaction.add(R.id.mFrameMain, mMainSettingFragment);
                } else {
                    transaction.show(mMainSettingFragment);
                }
                transaction.commit();
                break;
            case R.id.mFabAdd:
                //  TODO add...
                break;
        }
    }

    //
    private void resetTab() {
        mFootTvMusic.setTextColor(getResources().getColor(R.color.gray_deep));
        Drawable musicDraw = getResources().getDrawable(R.mipmap.icon_music);
        musicDraw.setBounds(0, 0, musicDraw.getMinimumWidth(), musicDraw.getMinimumHeight());
        mFootTvMusic.setCompoundDrawables(null, musicDraw, null, null);
        //
        mFootTvSetting.setTextColor(getResources().getColor(R.color.gray_deep));
        Drawable settingDraw = getResources().getDrawable(R.mipmap.icon_setting);
        settingDraw.setBounds(0, 0, settingDraw.getMinimumWidth(), settingDraw.getMinimumHeight());
        mFootTvSetting.setCompoundDrawables(null, settingDraw, null, null);
    }


    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (mMainMusicFragment != null) {
            fragmentTransaction.hide(mMainMusicFragment);
        }
        if (mMainSettingFragment != null) {
            fragmentTransaction.hide(mMainSettingFragment);
        }
    }
}
