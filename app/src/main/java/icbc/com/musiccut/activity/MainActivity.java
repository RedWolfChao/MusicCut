package icbc.com.musiccut.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatEditText;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import icbc.com.musiccut.R;
import icbc.com.musiccut.base.BaseActivity;
import icbc.com.musiccut.fragment.MainMusicFragment;
import icbc.com.musiccut.fragment.MainSettingFragment;
import icbc.com.musiccut.model.EventShowMenu;

/**
 * Created By RedWolf on 2018/10/12 10:11
 * MainActivity.java
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private AppCompatEditText mEtSearch;
    private TextView mFootTvMusic;
    private TextView mFootTvSetting;
    private ImageView mIvAdd;
    private MainMusicFragment mMainMusicFragment;
    private MainSettingFragment mMainSettingFragment;
    private boolean mCanBack;
    private ConstraintLayout mMenuLayout, mInMenuLayout;
    private ObjectAnimator mEnterAnimator;
    private ObjectAnimator mEnterInLayoutAnimator;
    private ObjectAnimator mExitAnimator;
    private ObjectAnimator mExitInLayoutAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initEvent();
        initMenu();
    }

    private void initMenu() {
        mMenuLayout = findViewById(R.id.mMenuLayout);
        mInMenuLayout = findViewById(R.id.mInMenuLayout);
        mInMenuLayout.animate().translationY(500);
        ImageView mIvMenuClose = findViewById(R.id.mIvMenuClose);
        mIvMenuClose.setOnClickListener(this);
        initAnim();
        //  拦截点击事件
        mMenuLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


    }

    private void initAnim() {
        //  外部透明度动画
        mEnterAnimator = ObjectAnimator.ofFloat(
                mMenuLayout, "alpha", 0, 1);
        mEnterAnimator.setDuration(300);
        mExitAnimator = ObjectAnimator.ofFloat(
                mMenuLayout, "alpha", 1, 0);
        mExitAnimator.setDuration(300);
        //  内部平移动画
        mEnterInLayoutAnimator = ObjectAnimator.ofFloat(
                mInMenuLayout, "translationY", 0);
        mEnterInLayoutAnimator.setDuration(300);
        mExitInLayoutAnimator = ObjectAnimator.ofFloat(
                mInMenuLayout, "translationY", 500);
        mExitInLayoutAnimator.setDuration(300);
        mExitAnimator.addListener(new AnimCallBack() {

            @Override
            public void onAnimationEnd(Animator animation) {
                mMenuLayout.setVisibility(View.GONE);
            }
        });

        mEnterAnimator.addListener(new AnimCallBack() {

            @Override
            public void onAnimationStart(Animator animation) {
                mMenuLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initData() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showMenu(EventShowMenu showMenu) {
        mEnterAnimator.start();
        mEnterInLayoutAnimator.start();
    }

    private void initView() {
        mEtSearch = findViewById(R.id.mEtSearch);
        mFootTvMusic = findViewById(R.id.mFootTvMusic);
        mFootTvSetting = findViewById(R.id.mFootTvSetting);
        mIvAdd = findViewById(R.id.mIvAdd);
        mEtSearch = findViewById(R.id.mEtSearch);
    }

    private void initEvent() {
        bindClickByView(this, mFootTvMusic, mFootTvSetting, mIvAdd);
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
            case R.id.mIvAdd:
                mEnterAnimator.start();
                mEnterInLayoutAnimator.start();
                break;
            case R.id.mIvMenuClose:
                mExitAnimator.start();
                mExitInLayoutAnimator.start();
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


    //  隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (mMainMusicFragment != null) {
            fragmentTransaction.hide(mMainMusicFragment);
        }
        if (mMainSettingFragment != null) {
            fragmentTransaction.hide(mMainSettingFragment);
        }
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        //  拦截back指令
        if (mCanBack) {
            System.exit(0);

        } else {
            ToastUtils.showShort("再按一次退出程序");
            mCanBack = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                        mCanBack = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    abstract class AnimCallBack implements Animator.AnimatorListener {

        @Override
        public void onAnimationEnd(Animator animation) {
            //  NONE
        }

        @Override
        public void onAnimationStart(Animator animation) {
            //  NONE
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            //  NONE
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            //  NONE
        }
    }
}
