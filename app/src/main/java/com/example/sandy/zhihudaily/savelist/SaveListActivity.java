package com.example.sandy.zhihudaily.savelist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sandy.zhihudaily.R;
import com.example.sandy.zhihudaily.listFrag.ListContract;

public class SaveListActivity extends AppCompatActivity {

    private SaveListPresenter presenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_list);
    }
}
