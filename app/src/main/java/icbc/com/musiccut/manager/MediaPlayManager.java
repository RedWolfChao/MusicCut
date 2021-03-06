package icbc.com.musiccut.manager;

import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import icbc.com.musiccut.callback.MusicPlayCallBack;

/**
 * Created By RedWolf on 2018/10/15 14:35
 * MediaPlayManager.java
 */

public class MediaPlayManager {
    private MediaPlayer sMediaPlayer;
    private MusicPlayCallBack mMusicPlayCallBack;
    private Timer mTimer;

    public void init(String musicPath, MusicPlayCallBack musicPlayCallBack) throws IOException {
        if (sMediaPlayer == null) {
            Uri uriPath = Uri.parse(musicPath);
            sMediaPlayer = MediaPlayer.create(Utils.getApp(), uriPath);
            this.mMusicPlayCallBack = musicPlayCallBack;
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (sMediaPlayer != null && sMediaPlayer.isPlaying()) {
                        if (mMusicPlayCallBack != null && sMediaPlayer != null) {
                            mMusicPlayCallBack.onMusicProgress(sMediaPlayer.getCurrentPosition());
                        }
                    }
                }
            }, 0, 800);
        }
    }

    /**
     * 播放音乐 或者 暂停音乐
     *
     * @throws IOException xx
     */
    public void playMusic() {
        if (sMediaPlayer != null) {
            if (sMediaPlayer.isPlaying()) {
                sMediaPlayer.pause();
            } else {
                sMediaPlayer.start();
            }
        }
    }

    /**
     * 停止播放音乐
     */
    public void stopMusic() {
        if (sMediaPlayer != null) {
            sMediaPlayer.stop();
            sMediaPlayer.release();
            sMediaPlayer = null;
            //
            mTimer.cancel();
            mTimer = null;
        }
    }

    /**
     * 拖拽到指定位置播放音频
     */
    public void seekTo(int second) {
        if (sMediaPlayer != null) {
            sMediaPlayer.seekTo(second);
        }
    }

    /**
     * 获取当前音乐播放状态
     */
    public boolean isPlaying() {
        return sMediaPlayer != null && sMediaPlayer.isPlaying();
    }


    public MediaPlayer getInstance() {
        return sMediaPlayer;
    }
}
