package icbc.com.musiccut.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import icbc.com.musiccut.R;
import icbc.com.musiccut.adapter.MenuAdapter;
import icbc.com.musiccut.base.BaseActivity;

public class MenuActivity extends BaseActivity {
    public static void actionStart(Context context){
        Intent intent = new Intent(context,MenuActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        RecyclerView mRecyclerViewMenu = findViewById(R.id.mRecyclerViewMenu);
        MenuAdapter mMenuAdapter = new MenuAdapter(this);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerViewMenu.setLayoutManager(mGridLayoutManager);
        mRecyclerViewMenu.setAdapter(mMenuAdapter);
    }
}
