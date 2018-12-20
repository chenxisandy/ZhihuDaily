package com.example.sandy.zhihudaily.data;

import android.graphics.Bitmap;

import com.example.sandy.zhihudaily.util.Value;

import java.io.Serializable;

public class Unit implements Serializable {
    private String content; //内容有2种形式，href 和 para

    private Value type;     //判定是段落还是文字链接

    private Bitmap img;     //图片，不可以：private Bitmap img = null;

    public Unit(String content, Value type) {
        this.content = content;
        this.type = type;
        img = null; //不可以在上面设置，毕竟。。
    }

    public Unit(Value type, Bitmap img) {
        this.type = type;
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public Value getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(Value type) {
        this.type = type;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
