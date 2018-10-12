package icbc.com.musiccut.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import icbc.com.musiccut.R;
import icbc.com.musiccut.base.BaseFragment;

/**
 * Created By RedWolf on 2018/10/11 13:56
 * 乐库-剪切
 */

public class CutMusicFragment extends BaseFragment {
    private static CutMusicFragment sCutMusicFragment;

    public static CutMusicFragment getInstance() {
        if (sCutMusicFragment == null) {
            sCutMusicFragment = new CutMusicFragment();
        }
        return sCutMusicFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.foot_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
