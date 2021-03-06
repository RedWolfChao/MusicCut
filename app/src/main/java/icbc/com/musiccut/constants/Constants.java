package icbc.com.musiccut.constants;

import android.Manifest;

/**
 * Created By RedWolf on 2018/10/15 16:42
 * Constants.java
 */

public class Constants {

    /* VISUALIZER */
    // 获取这些数据, 用于显示
    public static final int CAPTURE_SIZE = 1024;

    /* TAB */
    public static final String[] TABS = {"本地", "剪切", "合成", "转换", "其他"};
    /* EXTRA KEY */
    public static final String EXTRA_KEY_PROCESS_MUSIC_PATH = "EXTRA_KEY_PROCESS_MUSIC_PATH";
    public static final String EXTRA_KEY_PROCESS_NAME = "EXTRA_KEY_PROCESS_NAME";
    public static final String EXTRA_KEY_IS_FOR_RESULT = "EXTRA_KEY_IS_FOR_RESULT";


    /* PROCESS NAMES */
    public static final String PRECESS_NAME_CUT_MUSIC = "剪切音乐";

    /* REQUEST_CODES */
    public static final int REQUEST_CODE_GET_MUSIC_PATH = 10001;
    public static final int REQUEST_CODE_GET_PERMISSION = 10002;

    /* PERMISSIONS */
    // 权限
    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
}
