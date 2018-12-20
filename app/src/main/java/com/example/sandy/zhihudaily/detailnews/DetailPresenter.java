package com.example.sandy.zhihudaily.detailnews;

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

    DetailPresenter(DetailContract.View view, NewsDataSource repo) {
        this.view = view;
        this.repo = repo;
        view.setPresenter(this);
    }

    @Override
    public void loadDetail() {      //加载view显示出来,判断是从网络加载还是lite pal
        news = view.getNewsFromIntent();
//        if (news.getContentList().size() < 1) {     //如果没有，那就从网络加载
//            repo.loadDetail(news);
//        } else {
//            repo.loadDetail(news);          //否则加载我们的本地文件
//        }
        repo.loadDetail(news, new NewsDataSource.DetailListener() {     //在循环里面一个个地设置号unit
            @Override
            public void onLoadDetail() {
                for (int i = 0; i < news.getContentList().size(); i++) {
                    Unit unit = news.getContentList().get(i);
                    if (unit.getImg() == null && unit.getType() == Value.HREF) {       //如果有图片就不加载
                        unit.setType(Value.IMAGE);
                        repo.loadPic(unit.getContent(), (ImageView) view.addViews(Value.IMAGE), unit);    //在那个方法里面他已经设好了图片
                    } else {
                        ((TextView) view.addViews(Value.PARA)).setText(unit.getContent());  //文本就给他段落
                    }
                    view.showStar();
                }
            }
        });
        //以下为加载自定义view group的东西吧？

    }

    @Override
    public void starNews() {        //收藏新闻，包括下载新闻
        repo.changeNews(news);
//        if (repo instanceof LocalResource) {        //如果是local,改就对了
//            repo = RemoteSource.getInstance();
//        } else {
//            repo = LocalResource.getInstance();
//        }
        view.showStar();        //改变后表现出来
    }
}
