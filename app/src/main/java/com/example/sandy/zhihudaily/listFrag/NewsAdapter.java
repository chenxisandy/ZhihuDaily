package com.example.sandy.zhihudaily.listFrag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sandy.zhihudaily.R;
import com.example.sandy.zhihudaily.data.News;
import com.example.sandy.zhihudaily.data.Unit;
import com.example.sandy.zhihudaily.detailnews.DetailActivity;
import com.example.sandy.zhihudaily.util.Value;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> mNewsList;

    private Context mContext;

    private Value type;
    NewsAdapter(List<News> mNewsList, Value type) {
        this.mNewsList = mNewsList;
        this.type = type;
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.card_view:
//
//                break;
//                default:
//                    break;
//        }
//    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        // TODO: 2018/12/10 we can add cardView to change color

        TextView title;

        ImageView img;

        CardView cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);

            img = itemView.findViewById(R.id.img_title);

            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null) {
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        //News news = mNewsList.get(viewHolder.getAdapterPosition());
        final News news = mNewsList.get(i);
        if (news.getImgTitle() != null) {
            viewHolder.img.setImageBitmap(news.getImgTitle());
        }
        viewHolder.title.setText(news.getTitle());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);    //ListFragment 不行,只需要传个news就行
                intent.putExtra(mContext.getString(R.string.NEWS_INDEX), i);   //只能这样，因为我不知道怎么传i过去,只需要传指针，便可以获得我们的news
                intent.putExtra(mContext.getString(R.string.TYPE), type);
                mContext.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mNewsList.size();
    }


}
