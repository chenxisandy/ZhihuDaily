package com.example.sandy.zhihudaily.data;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class News extends DataSupport implements Serializable {

    private String title;

    //private String content;

    private String href;     //注意这个是部分href还是全部href加上daily.zhihu.com/于前面

    private String imgHref;

    private boolean isStar;     //大哥，这个要用到啊

    private List<Unit> contentList;

    private Bitmap imgTitle;


    public void setImgTitle(Bitmap imgTitle) {
        this.imgTitle = imgTitle;
    }

    public Bitmap getImgTitle() {

        return imgTitle;
    }

    public News(String title, String href, String imgHref) {
        this.title = title;
        this.href = href;
        this.imgHref = imgHref;
        contentList = new ArrayList<>();
        isStar = false;
        imgTitle = null;

    }

    public String getTitle() {
        return title;
    }

//    public String getContent() {
//        return content;
//    }

    public String getHref() {
        return href;
    }

    public String getImgHref() {
        return imgHref;
    }

    public void setContentList(List<Unit> contentList) {
        this.contentList = contentList;
    }

    public List<Unit> getContentList() {

        return contentList;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public void setContent(String content) {
//        this.content = content;
//    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setImgHref(String imgHref) {
        this.imgHref = imgHref;
    }

    public void setStar(boolean star) {
        isStar = star;
    }
    //    @Override
//    public boolean equals( Object obj) {    //重写equal保证可以不依赖id，仅仅凭哈希码就可以删除 用hashCode就行
//        if (this == obj) return true;
//        else return false;
//    }

}
