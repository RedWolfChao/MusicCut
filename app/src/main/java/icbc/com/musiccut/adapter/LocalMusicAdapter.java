package icbc.com.musiccut.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import icbc.com.musiccut.R;
import icbc.com.musiccut.callback.LocalMusicCallBack;
import icbc.com.musiccut.model.LocalMusicEntity;
import icbc.com.musiccut.utils.FileUtils;
import icbc.com.musiccut.utils.TimeUtils;

/**
 * Created By RedWolf on 2018/10/12 14:37
 * LocalMusicAdapter.java
 */

public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.MyViewHolder> {
    private List<LocalMusicEntity> mLocalMusicEntityList = new ArrayList<>();
    private Context mContext;
    private LocalMusicCallBack mLocalMusicCallBack;
    private boolean mIsSearch;
    private boolean mIsForResult;


    public LocalMusicAdapter(List<LocalMusicEntity> mLocalMusicEntityList, Context mContext, boolean isSearch, boolean isForResult) {
        this.mLocalMusicEntityList = mLocalMusicEntityList;
        this.mContext = mContext;
        this.mIsSearch = isSearch;
        this.mIsForResult = isForResult;
    }

    private LocalMusicAdapter(List<LocalMusicEntity> mLocalMusicEntityList, Context mContext, boolean isSearch) {
        this(mLocalMusicEntityList, mContext, isSearch, false);
    }


    public LocalMusicAdapter(List<LocalMusicEntity> mLocalMusicEntityList, Context mContext) {
        this(mLocalMusicEntityList, mContext, false);
    }

    public void addLocalMusicCallBack(LocalMusicCallBack localMusicCallBack) {
        this.mLocalMusicCallBack = localMusicCallBack;
    }

    public void setLocalMusicEntityList(List<LocalMusicEntity> localMusicEntityList) {
        this.mLocalMusicEntityList = localMusicEntityList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_local_music, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        LocalMusicEntity entity = mLocalMusicEntityList.get(position);
        String musicLong = String.format(mContext.getResources().getString(R.string.string_music_long), TimeUtils.millis2minute(entity.getMusicLong()) + "");
        String musicType = String.format(mContext.getResources().getString(R.string.string_music_size), FileUtils.byte2size(entity.getMusicSize()) + "");
        String musicSize = String.format(mContext.getResources().getString(R.string.string_music_type), entity.getMusicType() + "");
        holder.tvMusicLong.setText(musicLong);
        holder.tvMusicSize.setText(musicType);
        holder.tvMusicType.setText(musicSize);
        holder.tvMusicName.setText(entity.getMusicEasyName());
        //  Pinyin
        if (position == 0) {
            //  第一个位置肯定要显示拼音
            holder.tvPinyin.setVisibility(View.VISIBLE);
            holder.tvPinyin.setText(entity.getMusicPinyin());
            if (mIsSearch) {
                holder.tvPinyin.setBackgroundColor(Color.WHITE);
            }
        } else {
            //  如果当前拼音和上一个拼音相同,那么拼音Item不显示
            if (StringUtils.equals(entity.getMusicPinyin(), mLocalMusicEntityList.get(position - 1).getMusicPinyin())) {
                holder.tvPinyin.setVisibility(View.GONE);
            } else {
                holder.tvPinyin.setVisibility(View.VISIBLE);
                holder.tvPinyin.setText(entity.getMusicPinyin());
                if (mIsSearch) {
                    holder.tvPinyin.setBackgroundColor(Color.WHITE);
                }
            }
        }
        //  event
        holder.vgMusicInfo.setOnClickListener(v -> mLocalMusicCallBack.onItemClick(position));
        if (mIsForResult) {
            holder.ibMenu.setVisibility(View.GONE);
        } else {
            holder.ibMenu.setVisibility(View.VISIBLE);
            holder.ibMenu.setOnClickListener(v -> mLocalMusicCallBack.onMenuClick(position));
        }
    }

    @Override
    public int getItemCount() {
        return mLocalMusicEntityList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvPinyin;
        ViewGroup vgMusicInfo;
        TextView tvMusicName;
        TextView tvMusicLong;
        TextView tvMusicType;
        TextView tvMusicSize;
        ImageButton ibMenu;

        MyViewHolder(View itemView) {
            super(itemView);
            tvPinyin = itemView.findViewById(R.id.mTvPinyin);
            vgMusicInfo = itemView.findViewById(R.id.mConsLayoutMusicInfo);
            tvMusicName = itemView.findViewById(R.id.mTvProcessMusicName);
            tvMusicLong = itemView.findViewById(R.id.mTvMusicLong);
            tvMusicType = itemView.findViewById(R.id.mTvMusicType);
            tvMusicSize = itemView.findViewById(R.id.mTvMusicSize);
            ibMenu = itemView.findViewById(R.id.mIbMenu);
        }
    }
}
