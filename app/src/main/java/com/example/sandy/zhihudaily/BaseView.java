package com.example.sandy.zhihudaily;

public interface BaseView<T> {
    void setPresenter(T presenter); //这里用泛型
}
