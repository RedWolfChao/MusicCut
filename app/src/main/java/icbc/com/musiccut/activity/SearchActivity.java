package icbc.com.musiccut.activity;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import icbc.com.musiccut.R;
import icbc.com.musiccut.adapter.LocalMusicAdapter;
import icbc.com.musiccut.base.BaseActivity;
import icbc.com.musiccut.callback.LocalMusicCallBack;
import icbc.com.musiccut.callback.MusicPlayCallBack;
import icbc.com.musiccut.constants.Constants;
import icbc.com.musiccut.manager.MediaPlayManager;
import icbc.com.musiccut.model.LocalMusicEntity;
import icbc.com.musiccut.utils.ScanMusicUtils;
import icbc.com.musiccut.view.MenuDialog;
import icbc.com.musiccut.view.PlayDialog;

public class SearchActivity extends BaseActivity implements LocalMusicCallBack, DialogInterface.OnDismissListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener, MusicPlayCallBack{
    private TextView mTvBack;
    private EditText mEtSearch;
    private Button mBtnScan, mBtnPath;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<LocalMusicEntity> mLocalMusicList;
    private LinearLayoutManager mManager;
    private LocalMusicAdapter mLocalMusicAdapter;
    private PlayDialog mPlayDialog;
    private MediaPlayManager mMediaPlayManager;
    private Handler mHandler = new Handler();
    //
    private boolean mIsForResult;
    private Intent mIntent;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(Constants.EXTRA_KEY_IS_FOR_RESULT, false);
        context.startActivity(intent);
    }

    public static void actionStartForResult(BaseActivity baseActivity, int requestCode) {
        Intent intent = new Intent(baseActivity, SearchActivity.class);
        intent.putExtra(Constants.EXTRA_KEY_IS_FOR_RESULT, true);
        baseActivity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initParam();
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bindClickByView(this, mBtnPath);

    }

    private void filter(String aa) {
        Pattern pattern = Pattern.compile(aa);
        //
        List<LocalMusicEntity> mList = new ArrayList<>();
        for (int i = 0; i < mLocalMusicList.size(); i++) {
            LocalMusicEntity entity = mLocalMusicList.get(i);
            String musicName = entity.getMusicEasyName();
            Matcher matcher = pattern.matcher(musicName);
            //
            if (matcher.find()) {
                mList.add(entity);
            }
        }
        mLocalMusicAdapter = new LocalMusicAdapter(mList, SearchActivity.this, true, mIsForResult);
        mRecyclerView.setAdapter(mLocalMusicAdapter);
    }

    private void initParam() {
        mIntent = getIntent();
        mIsForResult = mIntent.getBooleanExtra(Constants.EXTRA_KEY_IS_FOR_RESULT, false);
    }

    private void initView() {
        mTvBack = findViewById(R.id.mTvBack);
        mEtSearch = findViewById(R.id.mEtSearch);
        mBtnScan = findViewById(R.id.mBtnScan);
        mBtnPath = findViewById(R.id.mBtnPath);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this::initMusicList);

    }

    private void initData() {
        mMediaPlayManager = new MediaPlayManager();
        initMusicList();
    }

    private void initMusicList() {
        //  读取本地音乐..
        if (mLocalMusicList == null) {
            mLocalMusicList = ScanMusicUtils.getMusicData(mActivity);
        } else {
            mLocalMusicList.clear();
            mLocalMusicList.addAll(ScanMusicUtils.getMusicData(mActivity));
        }
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (mLocalMusicList != null && mLocalMusicList.size() != 0) {
            Collections.sort(mLocalMusicList);
            if (mManager == null) {
                mManager = new LinearLayoutManager(mActivity);
                mRecyclerView.setLayoutManager(mManager);
            }
            if (mLocalMusicAdapter == null) {
                mLocalMusicAdapter = new LocalMusicAdapter(mLocalMusicList, mActivity, true, mIsForResult);
                mRecyclerView.setAdapter(mLocalMusicAdapter);
            } else {
                mLocalMusicAdapter.setLocalMusicEntityList(mLocalMusicList);
            }
            mLocalMusicAdapter.addLocalMusicCallBack(this);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showDialog(LocalMusicEntity entity) {
        //  初始化Dialog
        mPlayDialog = PlayDialog.build(mActivity,
                entity.getMusicLong(),
                entity.getMusicPath(),
                this, this, this);
        //  弹出
        mPlayDialog.show();
        //  播放音乐
        try {
            mMediaPlayManager.init(entity.getMusicPath(), this);
            mMediaPlayManager.playMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        //
        switch (v.getId()) {
            case R.id.mIvItemMusicPlay:
                //  Item
                //  播放||暂停音乐
                mMediaPlayManager.playMusic();
                //
                if (mMediaPlayManager.isPlaying()) {
                    mPlayDialog.pausePlay();
                } else {
                    mPlayDialog.startPlay();
                }
                break;
            case R.id.mBtnPath:
                showFileChooser();
                break;
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //  如果是用户滑动的才设置播放进度
        if (fromUser) {
            mMediaPlayManager.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //  TODO nothing ...

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //  TODO nothing ...
    }

    @Override
    public void onMusicProgress(final int progress) {
        mHandler.post(() -> {
            mPlayDialog.setMusicProgress(progress);
            mPlayDialog.setSeekBarProgress(progress);
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        //  Dialog 消失事件
        //  关音乐
        mMediaPlayManager.stopMusic();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "XXX"), Constants.REQUEST_CODE_GET_MUSIC);
        } catch (android.content.ActivityNotFoundException ex) {
            //  TODO 直接通过Intent跳转到应用市场让他下载..
            ToastUtils.showShort("请安装文件管理器");
        }
    }

    @Override
    public void onItemClick(int pos) {
        if (mIsForResult) {
            //  如果来自ForResult 则 setResult
            mIntent.putExtra(Constants.EXTRA_KEY_PROCESS_MUSIC, mLocalMusicList.get(pos));
            setResult(RESULT_OK, mIntent);
            finish();
        } else {
            //  否则 弹出音乐播放
            if (mPlayDialog != null &&
                    mPlayDialog.isShowing()) {
                mPlayDialog.cancelDialog();
            } else {
                showDialog(mLocalMusicList.get(pos));
            }
        }
    }

    @Override
    public void onMenuClick(int pos) {
        MenuDialog.build(this, mLocalMusicList.get(pos)).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUEST_CODE_GET_MUSIC:
//                if (mIsForResult) {
                //  如果来自ForResult 则 setResult
                //  TODO 需要做从 data 中获取Music信息
                LogUtils.iTag("RedWolf", "onActivityResult: ");
                mIntent.putExtra(Constants.EXTRA_KEY_PROCESS_MUSIC, mLocalMusicList.get(10));
                setResult(RESULT_OK, mIntent);
                finish();
//                }
                break;
        }
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        final String column = "_data";
        final String[] projection = {column};

        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
