package com.example.sandy.zhihudaily.data.local;

import com.example.sandy.zhihudaily.data.News;
import com.example.sandy.zhihudaily.data.NewsDataSource;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class LocalResource implements NewsDataSource.Local {    //lite pal在此发挥效果。

    private static List<News> newsList = new ArrayList<>();

    private static LocalResource INSTANCE;

    private LocalResource() {};

    public static LocalResource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LocalResource();
        }
        INSTANCE.newsList = DataSupport.findAll(News.class);    //这是初步加载时
        return INSTANCE;
    }

//    @Override
//    public void onNewsLoaded(NewsDataSource.DataListener listener, Context context) throws IOException {
//        listener.onLoad(newsList);
//    }         直接加载就行了，别管那么多

    @Override
    public News getNews(int index) {
        return newsList.get(index);
    }

    @Override
    public int getIndex(News news) {
        return newsList.indexOf(news);
    }

//    @Override
//    public void loadDetail(News news) {
//        // TODO: 2018/12/17 到底要不要加上呢，毕竟如果在本地库那么它就是存下了的
//    }


    @Override
    public List<News> getList() {
        return newsList;
    }

    @Override
    public void changeNews(News news) {     //由收藏而删除，从数据库中移除
        DataSupport.deleteAll(News.class, "title = ?", news.getTitle());    //同名就删除，毕竟文章不可能同名
        newsList.remove(news);  //也删除
    }

    @Override
    public void setListAdapter(NewsDataSource.DataListener dataListener) {
        dataListener.onLoad(newsList);
    }

    public void addNews(News news) {    //这个是在detail里面通过star给他加News
        newsList.add(news);
    }
}
