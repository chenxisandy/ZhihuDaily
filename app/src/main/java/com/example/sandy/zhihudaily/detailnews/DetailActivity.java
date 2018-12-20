package com.example.sandy.zhihudaily.detailnews;

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

    private int isInLocal;  //小于0就不在local

    private ImageView imageView;

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
        isInLocal = LocalResource.getInstance().getList().indexOf(news);
//        if (isInLocal < 0) { //当news不属于local时，代表加载remote
//            Glide.with(this).load(news.getImgHref()).into(imageView);todo how to handle this?
//            presenter = new DetailPresenter(this, RemoteSource.getInstance());
//        } else {
//            presenter = new DetailPresenter(this, LocalResource.getInstance());
//        }
        Value type = isInLocal < 0 ? Value.MAIN_LIST : Value.SAVE_LIST;
        presenter = new DetailPresenter(this, NewsDataRepo.getInstance(RemoteSource.getInstance(), LocalResource.getInstance(), type));
        presenter.loadDetail();     //加载一波数据
        //showStar(); 已经在listener里面完成，只有加载完我们才set这一切
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showStar() {
        if (isInLocal >= 0) {   //被收藏
            imageView.setBackground(getDrawable(R.drawable.star_yes));
        } else {
            imageView.setBackground(getDrawable(R.drawable.star_no));
        }
    }

    @Override
    public News getNewsFromIntent() {
        Intent intent = getIntent();
        news = (News) intent.getSerializableExtra(getString(R.string.NEWS));
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
                imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.addView(imageView);          //同上
                return imageView;
                default:
                    return null;    //感觉没有异常处理自己不道德
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.star:
                isInLocal = -isInLocal; //改变状态变符号
                presenter.starNews();       //开始改变收藏状态
                break;
                default:
                    break;
        }
    }
}
