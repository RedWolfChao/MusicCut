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
 * 乐库-合成
 */

public class SyntheticMusicFragment extends BaseFragment {
    private static SyntheticMusicFragment sSyntheticMusicFragment;

    public static SyntheticMusicFragment getInstance() {
        if (sSyntheticMusicFragment == null) {
            sSyntheticMusicFragment = new SyntheticMusicFragment();
        }
        return sSyntheticMusicFragment;
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
