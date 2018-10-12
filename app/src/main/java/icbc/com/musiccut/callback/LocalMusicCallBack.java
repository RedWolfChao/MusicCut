package icbc.com.musiccut.callback;

/**
 * Created By RedWolf on 2018/10/12 14:33
 * LocalMusicAdapter中的各种事件
 */

public interface LocalMusicCallBack {
    /**
     * 条目被点击
     *
     * @param pos pos
     */
    void onItemClick(int pos);

    /**
     * memu被点击
     *
     * @param pos pos
     */
    void onMenuClick(int pos);

}
