package com.example.sandy.zhihudaily.listFrag;

import com.example.sandy.zhihudaily.data.News;
import com.example.sandy.zhihudaily.mainList.ListPresenter;

import java.util.List;

public interface ListFragView {
    void changeFrag();  //加载完后改变frag为正常

    void refreshList(); //进行此时，其实在重新加载一遍,注意这个是重新刷新

    void setAdapter();  //在此设置好adapter

    void setList(List<News> newsList); //加载来得到list

    void setPresenter(ListContract.Presenter presenter);
}
