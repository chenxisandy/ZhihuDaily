package com.example.sandy.zhihudaily.mainList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.sandy.zhihudaily.data.News;
import com.example.sandy.zhihudaily.data.NewsDataSource;
import com.example.sandy.zhihudaily.listFrag.ListContract;
import com.example.sandy.zhihudaily.listFrag.ListFragView;

import java.io.IOException;
import java.util.List;

public class ListPresenter implements ListContract.Presenter {

    private ListFragView view;

    private NewsDataSource resourceRepo;

    private static final int CHANGE_FRAG = 1;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHANGE_FRAG:
                    view.setAdapter();
                    break;
                    default:
                        break;
            }
        }
    };

    ListPresenter(ListFragView view, NewsDataSource resourceRepo) {
        this.view = view;
        this.resourceRepo = resourceRepo;
        view.setPresenter(this);
    }

//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case CHANGE_FRAG:
//
//                    //如果是就改变frag来操作
//                    // TODO: 2018/12/13 在mainActivity里面加上一个static方法供此处，来改变frag 12/17：不行因为里面有静态方法
//                    break;
//            }
//        }
//    };


    /*
    * 究极监听者模式，presenter监听remote，
    * listActivity监听presenter
    * 前者是为了加载数据
    * 后者是为了数据加载完后改变frag
    * */
    @Override
    public void loadNewsList(final NewsDataSource.FragChangeListener fragChangeListener, Context context) {
        try {
            resourceRepo.onNewsLoaded(new NewsDataSource.DataListener() {
                @Override
                public void onLoad(List<News> newsList) {
                    fragChangeListener.changeFrag();
                    view.setList(newsList); //我们在这里设好view的set list,并且弄好适配器:不行，因为是UI操作先设好list再设adapter
                    Message message = new Message();
                    message.what = CHANGE_FRAG;
                    handler.sendMessage(message);   //发送过去叫他改frag
                }

//                @Override
//                public void onLoadCompleted() { //在此执行结束加载后，改变这些
//                    new FragChangeCallback() {
//                        @Override
//                        public void changeFrag(NewsDataSource.FragChangeListener listener) {
////                            listener.changeFrag();
//                        }
//                    }.changeFrag(fragChangeListener);
//                }
            }, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshList(Context context) {
        try {
            resourceRepo.onNewsLoaded(new NewsDataSource.DataListener() {
                @Override
                public void onLoad(List<News> newsList) {
                   view.setList(newsList);
                }

//                @Override
//                public void onLoadCompleted() {
//
//                }
            }, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public News getNews(int index) {    //得到news实际上调用model里面的东西
        return resourceRepo.getNews(index);
    }

    @Override
    public int getIndex(News news) {    //同上
        return resourceRepo.getIndex(news);
    }

    /*以下继承的是listener的东西*/

}
