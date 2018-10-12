package icbc.com.musiccut.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

import icbc.com.musiccut.utils.PinyinUtils;

/**
 * Created By RedWolf on 2018/10/12 13:40
 * LocalMusicEntity.java
 */

public class LocalMusicEntity implements Serializable, Comparable<LocalMusicEntity> {
    private String musicName;
    private String musicEasyName;
    private String musicId;
    private String musicAuthor;
    private String musicPath;
    private long musicLong;
    private String mimeType;
    private long musicSize;
    private String musicType;
    private String musicPinyin;

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public long getMusicSize() {
        return musicSize;
    }

    public void setMusicSize(long musicSize) {
        this.musicSize = musicSize;
    }

    public String getMusicType() {
        int index = this.musicName.lastIndexOf(".");
        return this.musicName.substring(index + 1);
    }

    public void setMusicType(String musicType) {
        this.musicType = musicType;
    }

    public long getMusicLong() {
        return musicLong;
    }

    public void setMusicLong(long musicLong) {
        this.musicLong = musicLong;
    }

    public String getMusicPinyin() {
        return PinyinUtils.getPinYinHeadChar(getMusicEasyName()).substring(0, 1);
    }

    public void setMusicPinyin(String musicPinyin) {
        this.musicPinyin = musicPinyin;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getMusicAuthor() {
        return musicAuthor;
    }

    public void setMusicAuthor(String musicAuthor) {
        this.musicAuthor = musicAuthor;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public String getMusicEasyName() {
        return musicEasyName;
    }

    public void setMusicEasyName(String musicEasyName) {
        this.musicEasyName = musicEasyName;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * 按照拼音排序
     */
    @Override
    public int compareTo(@NonNull LocalMusicEntity o) {
        char thisChar = this.getMusicPinyin().toCharArray()[0];
        char oChar = o.getMusicPinyin().toCharArray()[0];
        if (thisChar > oChar) {
            return 1;
        }
        if (thisChar == oChar) {
            return 0;
        }
        return -1;
    }
}
