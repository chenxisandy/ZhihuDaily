package com.example.sandy.zhihudaily.detailnews;

import android.content.Context;
import android.view.View;

import com.example.sandy.zhihudaily.BaseView;
import com.example.sandy.zhihudaily.data.News;
import com.example.sandy.zhihudaily.util.Value;

interface DetailContract {
    interface View {

        void setPresenter(DetailContract.Presenter presenter);

        void showStar();

        News getNewsFromIntent();

        android.view.View addViews(Value type);      //在实际加载过程中，view里先生成一个view，repo再给他值就行了

        Context getContext();
    }

    interface Presenter {

        void loadDetail();  //加载详情界面！！！包括model与 //先判定是不是缓存即content里面已经有内容

        void starNews();    //收藏新闻！ //包括change star 从空心变成实心 把东西丢到local里面

        News getNews(int index);

        int getIndex(News news);

    }
}
