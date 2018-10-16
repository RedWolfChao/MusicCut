package icbc.com.musiccut.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import icbc.com.musiccut.R;
import icbc.com.musiccut.constants.Constants;

/**
 * Created By RedWolf on 2018/10/15 16:39
 * MenuAdapter.java
 */


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private Context mContext;

    public MenuAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.mIvMenu.setImageDrawable(mContext.getResources().getDrawable(Constants.MENU_ICON_LIST[position]));
        holder.mTvMenu.setText(Constants.MENU_NAME_LIST[position]);
    }

    @Override
    public int getItemCount() {
        return Constants.MENU_NAME_LIST.length;
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView mTvMenu;
        ImageView mIvMenu;

        public MenuViewHolder(View itemView) {
            super(itemView);
            mTvMenu = itemView.findViewById(R.id.mTvMenu);
            mIvMenu = itemView.findViewById(R.id.mIvMenu);
        }
    }
}
