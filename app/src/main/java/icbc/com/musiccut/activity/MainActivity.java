package icbc.com.musiccut.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import icbc.com.musiccut.R;
import icbc.com.musiccut.base.BaseActivity;
import icbc.com.musiccut.constants.Constants;
import icbc.com.musiccut.fragment.MainMusicFragment;
import icbc.com.musiccut.fragment.MainSettingFragment;
import icbc.com.musiccut.permissions.PermissionsActivity;
import icbc.com.musiccut.permissions.PermissionsChecker;
import skin.support.SkinCompatManager;

/**
 * Created By RedWolf on 2018/10/12 10:11
 * MainActivity.java
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvSearch;
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

    private ImageView mIvMenuCut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initEvent();
        initMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PermissionsChecker checker = new PermissionsChecker(this);
        if (checker.lakesPermissions(Constants.PERMISSIONS)) {
            PermissionsActivity.actionStartForResult(this, Constants.REQUEST_CODE_GET_PERMISSION, Constants.PERMISSIONS);
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initMenu() {
        mMenuLayout = findViewById(R.id.mMenuLayout);
        mInMenuLayout = findViewById(R.id.mInMenuLayout);
        mInMenuLayout.animate().translationY(500);
        ImageView mIvMenuClose = findViewById(R.id.mIvMenuClose);
        mIvMenuClose.setOnClickListener(this);
        initAnim();
        //  拦截点击事件
        mMenuLayout.setOnTouchListener((v, event) -> true);


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
    }


    private void initView() {
        mTvSearch = findViewById(R.id.mEtSearch);
        mFootTvMusic = findViewById(R.id.mFootTvMusic);
        mFootTvSetting = findViewById(R.id.mFootTvSetting);
        mIvAdd = findViewById(R.id.mIvAdd);
        mTvSearch = findViewById(R.id.mTvSearch);
        mIvMenuCut = findViewById(R.id.mIvMenuCut);
    }

    private void initEvent() {
        bindClickByView(this, mFootTvMusic, mFootTvSetting, mIvAdd, mIvMenuCut);
        mTvSearch.setOnClickListener(this);
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
            case R.id.mTvSearch:
                SearchActivity.actionStart(mActivity);
                break;
            //  MENU_START
            case R.id.mIvMenuCut:
                MusicProcessActivity.actionStart(mActivity, Constants.PRECESS_NAME_CUT_MUSIC);
                mExitAnimator.start();
                mExitInLayoutAnimator.start();
                break;
            //  SKIN
            case R.id.mTvTheme:
            case R.id.mIvTheme:
                Log.i(TAG, "onClick: FUCK");
                SkinCompatManager.getInstance().loadSkin("blue.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                SkinCompatManager.getInstance().getCurSkinName();
                LogUtils.iTag("","");
                Log.i(TAG, "onClick: ");
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
        //  判断MenuLayout是否正在显示 如果正在显示 则使其消失
        if (mExitInLayoutAnimator != null && !mExitInLayoutAnimator.isRunning()) {
            if (mMenuLayout.getVisibility() == View.VISIBLE) {
                mExitInLayoutAnimator.start();
                mExitAnimator.start();
                return;
            }
        }

        //  拦截back指令
        if (mCanBack) {
            System.exit(0);
        } else {
            ToastUtils.showShort("再按一次退出程序");
            mCanBack = true;
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    mCanBack = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_GET_PERMISSION && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
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
