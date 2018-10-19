package icbc.com.musiccut.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import icbc.com.musiccut.R;
import icbc.com.musiccut.activity.MusicProcessActivity;
import icbc.com.musiccut.constants.Constants;
import icbc.com.musiccut.model.LocalMusicEntity;

/**
 * Created By RedWolf on 2018/10/15 9:29
 * PlayDialog.java
 */


public class MenuDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private LocalMusicEntity mLocalMusicEntity;


    private MenuDialog(@NonNull Context context, LocalMusicEntity entity) {
        super(context, R.style.MenuDialog);
        this.mContext = context;
        this.mLocalMusicEntity = entity;
    }

    @SuppressLint("StaticFieldLeak")
    private static MenuDialog menuDialog;


    public static MenuDialog build(Context context, LocalMusicEntity entity) {
        if (menuDialog != null) {
            menuDialog.cancelDialog();
        }
        menuDialog = new MenuDialog(context, entity);
        return menuDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_menu_layout);
        ImageView mIvMenuClose = findViewById(R.id.mIvMenuClose);
        TextView mTvTitle = findViewById(R.id.mTvTitle);
        ImageView mIvMenuCut = findViewById(R.id.mIvMenuCut);
        mTvTitle.setText(mLocalMusicEntity.getMusicEasyName());

        setOnClickListeners(mIvMenuClose, mIvMenuCut);

    }


    private void cancelDialog() {
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
            layoutParams.gravity = Gravity.CENTER_VERTICAL | Gravity.BOTTOM;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().getDecorView().setPadding(0, 0, 0, 0);
            getWindow().setAttributes(layoutParams);
        }
    }

    private void setOnClickListeners(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mIvMenuClose:
                cancelDialog();
                break;
            case R.id.mIvMenuCut:
                MusicProcessActivity.actionStart(mContext, Constants.PRECESS_NAME_CUT_MUSIC, mLocalMusicEntity);
                cancelDialog();
                break;
        }
    }
}
