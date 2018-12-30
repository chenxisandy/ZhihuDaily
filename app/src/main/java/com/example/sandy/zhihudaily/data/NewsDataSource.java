package com.example.sandy.zhihudaily.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.example.sandy.zhihudaily.util.Value;

import java.io.IOException;
import java.util.List;

public interface NewsDataSource {
    interface GlideBitMapCallBack {
        void getBitmap(Context context, String uri, final GlideBitmapListener listener);       //把glide从网络上加载成bitmap形式     若在本地就直接加
    }

    interface GlideBitmapListener {
        void getBitmapCallback(Bitmap bitmap);  //得到bitmap后把bitmap加载给需要的人
    }

    interface DataListener {
        void onLoad(List<News> list);       //不同于以前，集成了加载碎片
    }

    interface FragChangeCallback {
        void changeFrag(FragChangeListener listener);
    }

    interface FragChangeListener {
        void changeFrag();
//        void setAdapter();  //设置适配器
    }


    interface LoadNewsCallback {
        void onNewsLoaded(NewsDataSource.DataListener listener, Context context) throws IOException; //监听器只是把监听器自己发给callback，一旦callback完成就会回调并且告诉listener，所以实现callback的是实现者

    }

    interface DetailListener {
        void onLoadDetail();
    }

    void onNewsLoaded(NewsDataSource.DataListener listener, Context context) throws IOException;

    News getNews(int index);

    int getIndex(News news);

    List<News> giveList();

    void loadDetail(News news, DetailListener listener);      //用于详情界面

    void changeNews(News news); //改状态在收藏与不收藏之间

    void loadPic(String address, Context context, @Nullable Unit unit);

    interface Remote {
        News getNews(int index);

        int getIndex(News news);

        List<News> getList();

        void loadDetail(News news, DetailListener detailListener);      //用于详情界面

        void changeNews(News news); //改状态在收藏与不收藏之间

        void loadPic(String address, Context context, @Nullable Unit unit);
    }

    interface Local {
        News getNews(int index);

        int getIndex(News news);

        List<News> getList();

        //void loadDetail(News news);      //用于详情界面

        void changeNews(News news); //改状态在收藏与不收藏之间

        //void loadPic(ImageView view);

        void setListAdapter(DataListener dataListener);
    }
}
