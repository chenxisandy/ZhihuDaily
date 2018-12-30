package com.example.sandy.zhihudaily.mainList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.sandy.zhihudaily.R;
import com.example.sandy.zhihudaily.data.NewsDataRepo;
import com.example.sandy.zhihudaily.data.NewsDataSource;
import com.example.sandy.zhihudaily.data.local.LocalResource;
import com.example.sandy.zhihudaily.data.remote.RemoteSource;
import com.example.sandy.zhihudaily.listFrag.ListFragment;
import com.example.sandy.zhihudaily.listFrag.LoadFragment;
import com.example.sandy.zhihudaily.savelist.SaveListActivity;
import com.example.sandy.zhihudaily.util.Value;

import org.litepal.LitePal;

public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NewsDataSource.FragChangeListener {

    private static final String TAG = "navigation";
    private DrawerLayout mDrawerLayout;

    ListFragment mListFragment;

    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //初始化先生成数据库
        LitePal.getDatabase();
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.list_frag, new LoadFragment());
        transaction.commit();
        mListFragment = (ListFragment) mFragmentManager.findFragmentById(R.id.list_frag);
        if (mListFragment == null) {
            mListFragment = new ListFragment();
        }
        ListPresenter presenter = new ListPresenter(mListFragment, NewsDataRepo.getInstance(RemoteSource.getInstance(), LocalResource.getInstance(), Value.MAIN_LIST));
        presenter.loadNewsList(this, this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    public void changeFrag() {
        Bundle bundle = new Bundle();   //传东西给frag告诉它我是main list
        bundle.putSerializable(getString(R.string.LIST_TYPE), Value.MAIN_LIST); //我是main list就给你main list
        mListFragment.setArguments(bundle);  //有点想intent
        mDrawerLayout = findViewById(R.id.draw_layout);   //抽屉布局
//        Toolbar toolbar = mFragmentManager.findFragmentById(R.id.list_frag).getView().findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);  //用自己下载的图标以免那个太大
//        }
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);     // TODO: 2018/12/22 to find why this can't repley to me
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.list_frag, mListFragment);
        transaction.commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Log.d(TAG, "onNavigationItemSelected: lalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalalala");
        switch (menuItem.getItemId()) {
            case R.id.save_list:
                Intent intent = new Intent(this, SaveListActivity.class);
                Toast.makeText(this, "即将进入收藏界面", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.load:
                Toast.makeText(this, "暂时还不支持此功能", Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawers();
                break;
            default:
                break;
        }
        return true;
    }
}
