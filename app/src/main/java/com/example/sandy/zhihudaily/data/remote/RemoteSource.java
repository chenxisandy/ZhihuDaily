package com.example.sandy.zhihudaily.data.remote;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.sandy.zhihudaily.data.News;
import com.example.sandy.zhihudaily.data.NewsDataSource;
import com.example.sandy.zhihudaily.data.Unit;
import com.example.sandy.zhihudaily.data.local.LocalResource;
import com.example.sandy.zhihudaily.util.Value;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RemoteSource implements NewsDataSource.Remote, NewsDataSource.GlideBitMapCallBack, NewsDataSource.LoadNewsCallback { //我们在这里用单例模式

    private static List<News> newsList = new ArrayList<>();

    private static RemoteSource INSTANCE;

    private RemoteSource() {}   //空构造器


    public static RemoteSource getInstance() {    //只能这样获得实例
        if (INSTANCE == null) {
            INSTANCE = new RemoteSource();
        }
        return INSTANCE;
    }

    /**
     * 因为listener里面有一个model就可以调用model的onNewsLoad来，所以listener的第二个加载方法实现加载，而第一个给他只是为了让他填空
     */

    @Override
    public void onNewsLoaded(final NewsDataSource.DataListener listener, final Context context) {   //listener里面弄一个callback调用这个方法，把自己传进去，callback方法
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("https://daily.zhihu.com/").get();
                    Elements elements = document.select("a.link-button");
                    Elements elePics = document.select("img.preview-image");
                    for (int i = 0; i < elements.size(); i++) {
                        Element element = elements.get(i);
                        Element eleImg = elePics.get(i);
                        String href = element.attr("href");
                        String imgHref = eleImg.attr("src");
                        final News news = new News(element.text(), href, imgHref);
                        getBitmap(context, news.getImgHref(), new NewsDataSource.GlideBitmapListener() {
                            @Override
                            public void getBitmapCallback(Bitmap bitmap) {
                                news.setImgTitle(bitmap);
                            }
                        });
                        newsList.add(news);
                    }
                    // 最后把数据传给listener
                    listener.onLoad(newsList);
//                    listener.onLoadCompleted(); //先把数据给他，再改frag
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public News getNews(int index) {
        return newsList.get(index);
    }

    @Override
    public int getIndex(News news) {
        return newsList.indexOf(news);
    }

    //此处对应的是detail list部分
    @Override
    public void loadDetail(final News news, final NewsDataSource.DetailListener detailListener) {  //在此处应该包括把东西给presenter->listener,所以说presenter与model通信是listener
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // TODO: 2018/12/17 看看要不要手动分段
                    List<Unit> unitList = null;
                    Document dc = Jsoup.connect("https://daily.zhihu.com" + news.getHref()).get();      //得到详情界面的网址
                    Elements elements = dc.select("p, img.content-image");
                    for (int i = 0; i < elements.size(); i++) {
                        String picHref = elements.get(i).attr("src");
                        String para = elements.get(i).text();
                        if (para != null && para.length() > 0) {
                            unitList.add(new Unit(para, Value.PARA));
                        } else {
                            unitList.add(new Unit(picHref, Value.HREF));    //在presenter中再转变为image
                        }
                        detailListener.onLoadDetail();
                    }
                    news.setContentList(unitList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // TODO: 2018/12/11 必须要开新线程，因为主线程只能进行UI操作
            }
        }).start();
    }

    @Override
    public List<News> getList() {  //暴露出这一个接口给他们用
        return newsList;
    }

    @Override
    public void changeNews(News news) {     //下载下来变为收藏，但是小心刷新问题，如何在这里改变local
//        if (news.getContentList().size() < 1) { //没有缓存或者缓存失败时
//            for (int i = 0; i < news.getContentList().size(); i++) {
//                final Unit unit = news.getContentList().get(i);
//                if (unit.getType() != Value.PARA) {
//                    new NewsDataSource.GlideBitmapListener() {
//                        @Override
//                        public void getBitmapCallback(Bitmap bitmap) {
//                            unit.setImg(bitmap);        //监听到了这些就设置一下
//                            unit.setType(Value.IMAGE);
//                        }
//                    };
//                }
//            }
//        }                 //标准的监听者模式的反面教材：不要删除了，悠着点
            //按理我们最初的时候就已经缓存了下来，直接丢入数据库就行
            LocalResource.getInstance().getList().add(news);   //这个封装感觉有点问题，以后小心，然后把数据添加到我们的local库
            news.save();        //最后设置完毕存到我们的数据库

    }
    @Override
    public void loadPic(String address, ImageView view, final Unit unit) {       //在presenter中可用，这句没什么问题
        Glide.with(view.getContext()).load(address).into(view);
        getBitmap(view.getContext(), address, new NewsDataSource.GlideBitmapListener() {
            @Override
            public void getBitmapCallback(Bitmap bitmap) {
                unit.setImg(bitmap);        //把图片加载到里面，在你显示出来时也顺便加载出来,缓存在内存里面，虽然感觉这样占内存有点坑
            }
        });
        // TODO: 2018/12/17 暂时还没有实现加载gif分为bitmap多帧
    }

    @Override
    public void getBitmap(Context context, String uri,@Nullable final NewsDataSource.GlideBitmapListener listener) {    //存储的时候要用到就去实现listener
//        Glide.with(context)
//                .load(uri)
//                .asBitmap()
//                .centerCrop()
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        listener.getBitmapCallback(resource);
//                    }
//                });
        //因为上面那个不能在其他线程运行，因此我们换个线程
        try {
            Bitmap bitmap = Glide.with(context)
                    .load(uri)
                    .asBitmap()
                    .centerCrop()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            listener.getBitmapCallback(bitmap);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
