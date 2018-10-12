package icbc.com.musiccut.utils;

/**
 * Created By RedWolf on 2018/10/12 17:14
 * FileUtils.java
 */

public class FileUtils {
    /**
     * 字节转可视单位
     */
    public static String byte2size(long byteZ) {
        if (byteZ < 1024) {
            return "1KB";
        }
        if (byteZ < 1024 * 1024) {
            return byteZ / 1024 + "KB";
        }
        if (byteZ < 1024 * 1024 * 1024) {
            String mb = byteZ / 1024 / 1024 + "";
            float kb = (byteZ / 1024) % 1024;
            float mb1 = kb / 1024;
            String ret = mb + ("" + mb1).substring(1, 4);
            return ret + "MB";
        }
        return ">999MB";
    }


}
