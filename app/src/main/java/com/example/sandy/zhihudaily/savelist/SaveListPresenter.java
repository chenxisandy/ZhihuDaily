package com.example.sandy.zhihudaily.savelist;

import android.content.Context;
import com.example.sandy.zhihudaily.data.News;
import com.example.sandy.zhihudaily.data.NewsDataSource;
import com.example.sandy.zhihudaily.listFrag.ListContract;
import com.example.sandy.zhihudaily.listFrag.ListFragView;

public class SaveListPresenter implements ListContract.Presenter {

    private ListFragView view;

    private NewsDataSource repo;

    SaveListPresenter(ListFragView view, NewsDataSource localRepo) {
        this.view = view;
        this.repo = localRepo;
        view.setPresenter(this);
    }


    @Override
    public void loadNewsList(NewsDataSource.FragChangeListener listener, Context context) {
        refreshList(context);
    }

    @Override
    public void refreshList(Context context) {
        view.setList(repo.giveList());
        view.setAdapter();      //适配器加载就好
    }

    @Override
    public News getNews(int index) {
        return repo.getNews(index);
    }

    @Override
    public int getIndex(News news) {
        return repo.getIndex(news);
    }
}
