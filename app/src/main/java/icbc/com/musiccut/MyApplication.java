package icbc.com.musiccut;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * Created by RedWolfChao on 2018/10/12.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
