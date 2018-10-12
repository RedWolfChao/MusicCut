package icbc.com.musiccut.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by RedWolfChao on 2018/10/12.
 */

public class BaseFragment extends Fragment {
    protected Context mContext;
    protected Activity mActivity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        mActivity = getActivity();
    }

   /* protected <T extends View> T findViewById(@IdRes int id) {
        if (mActivity != null) {
            return mActivity.findViewById(id);
        }
        return null;
    }*/


}
