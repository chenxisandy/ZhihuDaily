package com.example.sandy.zhihudaily.listFrag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sandy.zhihudaily.R;

public class LoadFragment extends Fragment {    //什么都不干，只是弄个图片来表示加载
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.list_frag, container, false);
        return root;        //应该加载一下就行
    }
}
