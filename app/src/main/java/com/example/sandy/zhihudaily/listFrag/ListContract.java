package com.example.sandy.zhihudaily.listFrag;

import android.content.Context;

import com.example.sandy.zhihudaily.data.News;
import com.example.sandy.zhihudaily.data.NewsDataSource;
public interface ListContract {

    //先一个fragment仅仅只能用一个view
//    interface View {
//        void changeFrag();  //加载完后改变frag为正常
//
//        void refreshList(); //进行此时，其实在重新加载一遍,注意这个是重新刷新
//
//        List<News> getList(); //加载来得到list
//    }

    interface Presenter {
        void loadNewsList(NewsDataSource.FragChangeListener listener, Context context);    //里面应该包括model的加载和view的换界面

        void refreshList(Context context);

        News getNews(int index);

        int getIndex(News news);

    }
}
