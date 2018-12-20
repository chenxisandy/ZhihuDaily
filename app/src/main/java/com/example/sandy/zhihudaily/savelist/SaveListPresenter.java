package com.example.sandy.zhihudaily.savelist;

import android.content.Context;

import com.example.sandy.zhihudaily.data.News;
import com.example.sandy.zhihudaily.data.NewsDataSource;
import com.example.sandy.zhihudaily.listFrag.ListContract;
import com.example.sandy.zhihudaily.listFrag.ListFragView;

public class SaveListPresenter implements ListContract.Presenter {

    private ListFragView view;

    private NewsDataSource.Local localRepo;

    public SaveListPresenter(ListFragView view, NewsDataSource.Local localRepo) {
        this.view = view;
        this.localRepo = localRepo;
        view.setPresenter(this);
    }


    @Override
    public void loadNewsList(NewsDataSource.FragChangeListener listener, Context context) {
        view.setList(localRepo.getList());
    }

    @Override
    public void refreshList(Context context) {

    }

    @Override
    public News getNews(int index) {
        return localRepo.getNews(index);
    }

    @Override
    public int getIndex(News news) {
        return localRepo.getIndex(news);
    }
}
