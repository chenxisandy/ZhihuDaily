package com.example.sandy.zhihudaily.data;

import android.content.Context;
import android.support.annotation.Nullable;

import com.example.sandy.zhihudaily.util.Value;

import java.io.IOException;
import java.util.List;

public class NewsDataRepo implements NewsDataSource {

    private static NewsDataSource.Remote mRemoteRepo;

    private static NewsDataSource.Local mLocalRepo;

    private static NewsDataRepo INSTANCE = null;

    private Value type;

    private NewsDataRepo(NewsDataSource.Remote remoteRepo, NewsDataSource.Local localRepo) {
        mLocalRepo = localRepo;
        mRemoteRepo = remoteRepo;
    }  //私有的构造器确保单例模式

    public static NewsDataRepo getInstance(NewsDataSource.Remote remoteRepo, NewsDataSource.Local localRepo, Value type) {
        if (INSTANCE == null) {
            INSTANCE = new NewsDataRepo(remoteRepo, localRepo);
        }
        INSTANCE.type = type;
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void onNewsLoaded(DataListener listener, Context context) throws IOException {
        if (type == Value.MAIN_LIST) {      //作为管控的人管理各个东西
            ((LoadNewsCallback) mRemoteRepo).onNewsLoaded(listener, context);
        } else {
            mLocalRepo.setListAdapter(listener);
        }
    }

    @Override
    public News getNews(int index) {
        if (type == Value.MAIN_LIST) {
            return mRemoteRepo.getNews(index);
        } else {
            return mLocalRepo.getNews(index);
        }
    }

    @Override
    public int getIndex(News news) {
        return type == Value.MAIN_LIST ? mRemoteRepo.getIndex(news) : mLocalRepo.getIndex(news);
    }

    @Override
    public List<News> giveList() {
        return type == Value.MAIN_LIST ? mRemoteRepo.getList() : mLocalRepo.getList();
    }

    @Override
    public void loadDetail(News news, DetailListener listener) {
        if (news.getContentList().size() > 0) {
            return;     //如果曾经缓存过了就无需再加载
        }
        if (type == Value.MAIN_LIST) {
            mRemoteRepo.loadDetail(news, listener);
        }
        //如果是local那么他已经存在数据库中直接调用便是
    }

    @Override
    public void changeNews(News news) {
        if (news.isStar()) {        //如果已经转变成了star
            mRemoteRepo.changeNews(news);
            type = Value.SAVE_LIST;
        } else {
            mLocalRepo.changeNews(news);
            type = Value.MAIN_LIST;
        }
    }

    @Override
    public void loadPic(String address, Context context, @Nullable Unit unit) {
        if (type == Value.MAIN_LIST)
            mRemoteRepo.loadPic(address, context, unit);
    }

    public Value getType() {
        return type;
    }

    public void setType(Value type) {
        this.type = type;
    }
}
