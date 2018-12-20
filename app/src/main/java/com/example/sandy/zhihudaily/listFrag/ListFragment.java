package com.example.sandy.zhihudaily.listFrag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sandy.zhihudaily.R;
import com.example.sandy.zhihudaily.data.News;
import com.example.sandy.zhihudaily.data.NewsDataSource;
import com.example.sandy.zhihudaily.util.Value;

import java.util.List;

public class ListFragment extends Fragment implements ListFragView, SwipeRefreshLayout.OnRefreshListener {

    private AppCompatActivity mAppCompatActivity;           //这个没有初始化怎么破？

    private Value listType;   //碎片对应的list的类型

    private NewsAdapter adapter;

    private ListContract.Presenter presenter;         //在此它不顾是remote还是local

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView mRecyclerView;

    private View root;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.list_frag, container, false);  //root代表根view，布局的一切都是view，这下订好了root就不会与别人混淆

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            listType = (Value) bundle.getSerializable(getString(R.string.LIST_TYPE));
        }
        initView();
        return root;
    }

    private void initView() {
        //find view
        Toolbar toolbar = root.findViewById(R.id.tool_bar);
        CollapsingToolbarLayout collapsingToolbarLayout = root.findViewById(R.id.collapse_toolbar);
        swipeRefreshLayout = root.findViewById(R.id.refresh_layout);
        mRecyclerView = root.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mAppCompatActivity, 1);
        // TODO: 2018/12/12 在此处添加一个可以加list的presenter方法里面要用它自己view里面的东西
        mRecyclerView.setLayoutManager(gridLayoutManager);     // todo to 检验

        mAppCompatActivity = (AppCompatActivity) getActivity(); //先确保得到
        if (mAppCompatActivity != null) {
            mAppCompatActivity.setSupportActionBar(toolbar);
        }

        collapsingToolbarLayout.setTitle(listType == Value.MAIN_LIST ? "日报" : "收藏");    //如果是mainlist我们就显示日报

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));
        swipeRefreshLayout.setOnRefreshListener(this);  //点击会启动refresh的方法

        presenter.loadNewsList((NewsDataSource.FragChangeListener) mAppCompatActivity, getContext());
    }


    @Override
    public void changeFrag() {  //改变碎片，是加载完的通知对象之一
        ((NewsDataSource.FragChangeListener) mAppCompatActivity).changeFrag();
    }

    @Override
    public void refreshList() {
        presenter.loadNewsList(new NewsDataSource.FragChangeListener() {
            @Override
            public void changeFrag() {
                //we do not have to do anything
            }
        }, getContext());       //加载list
    }

    @Override
    public void setAdapter() {
        //        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setList(List<News> newsList) {      //这些相当于在分线程中执行，所以不可以弄与UI有关的东西
        adapter = new NewsAdapter(newsList);
    }

    @Override
    public void setPresenter(ListContract.Presenter listPresenter) {
        presenter = listPresenter;  //这样就可以保持同步化
    }


    @Override
    public void onRefresh() {
        Toast.makeText(mAppCompatActivity, "刷新中", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.refreshList(getContext());
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }).start();     // TODO: 2018/12/17 好奇：这个start是怎么写出来的，尝试了但是不行
    }
}
