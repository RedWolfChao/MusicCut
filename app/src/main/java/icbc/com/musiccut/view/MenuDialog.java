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

import com.blankj.utilcode.util.LogUtils;

import icbc.com.musiccut.R;

/**
 * Created By RedWolf on 2018/10/15 9:29
 * PlayDialog.java
 */


public class MenuDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private ImageView mIvMenuClose;


    private MenuDialog(@NonNull Context context) {
        super(context, R.style.MenuDialog);
        this.mContext = context;
    }

    private static MenuDialog menuDialog;

    public static MenuDialog build(Context context) {
        if (menuDialog != null) {
            menuDialog.cancelDialog();
        }
        menuDialog = new MenuDialog(context);
        return menuDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_menu_layout);
        mIvMenuClose = findViewById(R.id.mIvMenuClose);
        mIvMenuClose.setOnClickListener(this);

    }


    public void cancelDialog() {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mIvMenuClose:
                cancelDialog();
                break;
        }
    }
}
