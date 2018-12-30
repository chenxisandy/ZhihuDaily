package com.example.sandy.zhihudaily.savelist;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sandy.zhihudaily.R;
import com.example.sandy.zhihudaily.data.NewsDataRepo;
import com.example.sandy.zhihudaily.data.local.LocalResource;
import com.example.sandy.zhihudaily.data.remote.RemoteSource;
import com.example.sandy.zhihudaily.listFrag.ListContract;
import com.example.sandy.zhihudaily.listFrag.ListFragment;
import com.example.sandy.zhihudaily.util.Value;

public class SaveListActivity extends AppCompatActivity {

    private SaveListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_list);
        initView();
    }

    private void initView() {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        ListFragment listFragment = (ListFragment) mFragmentManager.findFragmentById(R.id.save_list_frag);
        Bundle bundle = new Bundle();   //传东西给frag告诉它我是main list
        bundle.putSerializable(getString(R.string.LIST_TYPE), Value.SAVE_LIST);
        if (listFragment != null) {
            listFragment.setArguments(bundle);  //有点想intent
        }
//        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//        transaction.replace(R.id.save_list_frag, listFragment);
//        transaction.commit();
        if (listFragment != null) {
            presenter = new SaveListPresenter(listFragment, NewsDataRepo.getInstance(RemoteSource.getInstance(),
                    LocalResource.getInstance(), Value.SAVE_LIST));
        }
        presenter.loadNewsList(null, this); //去加载吧
    }
}
