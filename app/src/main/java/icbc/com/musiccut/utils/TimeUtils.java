package icbc.com.musiccut.utils;

/**
 * Created by RedWolfChao on 2018/10/12.
 */

public class TimeUtils {
    /**
     * 毫秒转分
     *
     * @param millis xx
     * @return xx
     */
    public static String millis2minute(long millis) {
        String minute = millis / 1000 / 60 + "";
        String second = (millis / 1000) % 60 + "";
        //
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        if (second.length() == 1) {
            second = "0" + second;
        }
        return minute + ":" + second;
    }
}
