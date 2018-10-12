package icbc.com.musiccut.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import icbc.com.musiccut.model.LocalMusicEntity;

/**
 * Created By RedWolf on 2018/10/12 15:30
 * 扫描本地音乐.java
 */

public class ScanMusicUtils {
    /**
     * 扫描系统中的音频文件
     *
     * @param context xx
     * @return xx
     */
    public static List<LocalMusicEntity> getMusicData(Context context) {
        List<LocalMusicEntity> retDataList = new ArrayList<>();
        if (context == null) {
            return retDataList;
        }
        LogUtils.iTag("RedWolf", "getMusicData: " + context);
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            LogUtils.iTag("RedWolf", "getMusicData: INTO");
            while (cursor.moveToNext()) {
                LogUtils.iTag("RedWolf", "getMusicData: FUCK!!!");
                LocalMusicEntity entity = new LocalMusicEntity();
                entity.setMusicName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                entity.setMusicEasyName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                entity.setMusicId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
                entity.setMusicAuthor(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));

                entity.setMusicPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                entity.setMusicLong(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                entity.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)));
                entity.setMusicSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                retDataList.add(entity);
                /*
                 LogUtils.iTag("RedWolf", "getMusicData: ",
                 "ALBUM = " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)),
                 "ALBUM_ID = " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)),
                 "ARTIST = " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)),
                 "DISPLAY_NAME = " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)),
                 "DATA = " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)),
                 "DURATION = " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)),
                 "MIME_TYPE = " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)),
                 "SIZE = " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)),
                 "TITLE = " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                 [1] = ALBUM = 不要再来伤害我 新歌+精选/
                 [2] = ALBUM_ID = 3/
                 [4] = ARTIST = 张振宇/
                 [9] = DISPLAY_NAME = 张振宇 - 不要再来伤害我.mp3/
                 [11] = DATA = /storage/emulated/0/netease/cloudmusic/Music/张振宇 - 不要再来伤害我.mp3/
                 [14] = DURATION = 324075/
                 [20] = MIME_TYPE = audio/mpeg/
                 [21] = SIZE = 5187225/
                 [22] = TITLE = 不要再来伤害我/
                 */
            }
            cursor.close();
        }
        return retDataList;
    }
}
