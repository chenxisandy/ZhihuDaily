package com.example.sandy.zhihudaily.detailnews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sandy.zhihudaily.R;
import com.example.sandy.zhihudaily.data.News;
import com.example.sandy.zhihudaily.data.NewsDataRepo;
import com.example.sandy.zhihudaily.data.local.LocalResource;
import com.example.sandy.zhihudaily.data.remote.RemoteSource;
import com.example.sandy.zhihudaily.util.Value;

public class DetailActivity extends AppCompatActivity implements DetailContract.View, View.OnClickListener {

    private News news;

    private DetailContract.Presenter presenter;

    private LinearLayout linearLayout;

    private ImageView imageView;

    private Value RepoType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//        Intent intent = getIntent();
//        news = (News) intent.getSerializableExtra(getString(R.string.NEWS));
//        Value type = (Value) intent.getSerializableExtra()    //提醒：不要删除，这是一个重要教训：如果仅仅依靠从是从哪个list里加载，你会发现许多下载的仍然会出现在Mainlist
        news = getNewsFromIntent();
        ImageView imgTitle = findViewById(R.id.img_title);
        imgTitle.setImageBitmap(news.getImgTitle());    // TODO: 2018/12/20 考虑要不要判断是否已经加载
        imageView = findViewById(R.id.star);
        imageView.setOnClickListener(this);
        linearLayout = findViewById(R.id.view_group);
        presenter = new DetailPresenter(this, NewsDataRepo.getInstance(RemoteSource.getInstance(), LocalResource.getInstance(), RepoType));
        presenter.loadDetail();     //加载一波数据
        //showStar(); 已经在listener里面完成，只有加载完我们才set这一切,所以不需要再写
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showStar() {
        if (RepoType == Value.SAVE_LIST || news.isStar()) {   //被收藏
            imageView.setBackground(getDrawable(R.drawable.star_yes));
        } else {
            imageView.setBackground(getDrawable(R.drawable.star_no));
        }
    }

    @Override
    public News getNewsFromIntent() {
        Intent intent = getIntent();
        int index = intent.getIntExtra(getString(R.string.NEWS_INDEX), 0);
        RepoType = (Value) intent.getSerializableExtra(getString(R.string.TYPE));
//        news = presenter.getNews(index);        //如此确保得到是引用而不是按值传
        news = RemoteSource.getInstance().getList().get(index);
        return news;
    }

    @Override
    public View addViews(Value type) {
        switch (type) {
            case PARA:
                TextView textView = new TextView(this); //新建一个view
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.addView(textView);         //最后要添加到这个view group里面来
                return textView;
            case IMAGE:
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.addView(imageView);          //同上
                return imageView;
                default:
                    return null;    //感觉没有异常处理自己不道德
        }
    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.star:
                if (RepoType == Value.MAIN_LIST) {  //改变一下这里的属性状态
                    RepoType = Value.SAVE_LIST;
//                    news.setStar(true);       这种事应该交给presenter去做
                } else {
                    RepoType = Value.MAIN_LIST;
//                    news.setStar(false);
                }
                presenter.starNews();       //开始改变收藏状态
                break;
                default:
                    break;
        }
    }
}
