package com.example.sandy.zhihudaily.detailnews;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sandy.zhihudaily.data.News;
import com.example.sandy.zhihudaily.data.NewsDataSource;
import com.example.sandy.zhihudaily.data.Unit;
import com.example.sandy.zhihudaily.util.Value;

public class DetailPresenter implements DetailContract.Presenter {

    private DetailContract.View view;

    private NewsDataSource repo; //待定，根据情况看他是local或remote

    private News news;

    @SuppressLint("HandlerLeak")
    private Handler ParaHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {    //用i传坐标与值
            ((TextView) view.addViews(Value.PARA)).setText(news.getContentList().get(msg.what).getContent());
            super.handleMessage(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler ImageHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ((ImageView) view.addViews(Value.IMAGE)).setImageBitmap(news.getContentList().get(msg.what).getImg());
            super.handleMessage(msg);
        }
    };

    DetailPresenter(DetailContract.View view, NewsDataSource repo) {
        this.view = view;
        this.repo = repo;
        view.setPresenter(this);
    }

    @Override
    public void loadDetail() {      //加载view显示出来,判断是从网络加载还是lite pal
        news = view.getNewsFromIntent();
        if (news.getContentList().size() > 0) {     //曾经加载完就调用缓存直接设置好view
            for (int i = 0; i < news.getContentList().size(); i++) {
                Unit unit = news.getContentList().get(i);
                if (unit.getType() == Value.PARA) {
                    ((TextView) view.addViews(Value.PARA)).setText(news.getContentList().get(i).getContent());
                } else if (unit.getType() == Value.IMAGE) {
                    ((ImageView) view.addViews(Value.IMAGE)).setImageBitmap(news.getContentList().get(i).getImg());
                }
            }
        } else {        //没有缓存就去缓存
            repo.loadDetail(news, new NewsDataSource.DetailListener() {     //在循环里面一个个地设置号unit

                @Override
                public void onLoadDetail() {
                    for (int i = 0; i < news.getContentList().size(); i++) {
                        final Unit unit = news.getContentList().get(i);
                        if (unit.getImg() == null && unit.getType() == Value.HREF) {       //如果有图片就不加载
                            unit.setType(Value.IMAGE);
                            repo.loadPic(unit.getContent(), view.getContext(), unit);    //在那个方法里面他已经设好了图片
                            Message message = new Message();
                            message.what = i;
                            ImageHandle.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = i;   //把i即下标传给它
                            ParaHandler.sendMessage(message);
                        }
                    }
                }
            });
        }
        //以下为加载自定义view group的东西吧？
        view.showStar();
    }

    @Override
    public void starNews() {        //收藏新闻，包括下载新闻
        news.setStar(!news.isStar());       //简单就好
        repo.changeNews(news);
//        if (repo instanceof LocalResource) {        //如果是local,改就对了
//            repo = RemoteSource.getInstance();
//        } else {
//            repo = LocalResource.getInstance();
//        }
        view.showStar();        //改变后表现出来
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
