package com.example.tst2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Set;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> news;
    private Set<String> nids;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        if (itemClickListener != null) {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getPosition();
                    itemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news1 = news.get(position);
        holder.titleTxt.setText(news1.Title);
        holder.sourceTxt.setText("来源：" + news1.Source);
        holder.timeTxt.setText("时间：" + news1.Time);
        if (nids.contains(position + "")) {
            holder.timeTxt.setTextColor(Color.parseColor("#0000FF"));
            holder.sourceTxt.setTextColor(Color.parseColor("#0000FF"));
            holder.titleTxt.setTextColor(Color.parseColor("#0000FF"));
        }
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public void Read(ViewHolder view) {
        view.timeTxt.setTextColor(Color.parseColor("#0000FF"));
        view.sourceTxt.setTextColor(Color.parseColor("#0000FF"));
        view.titleTxt.setTextColor(Color.parseColor("#0000FF"));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, sourceTxt, timeTxt;
        RelativeLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            layout = (RelativeLayout)itemView.findViewById(R.id.layout);
            titleTxt = (TextView)itemView.findViewById(R.id.titleTextview);
            sourceTxt = (TextView)itemView.findViewById(R.id.sourceTxtview);
            timeTxt = (TextView)itemView.findViewById(R.id.timeTxtview);
        }
    }
    public NewsAdapter(List<News> _news, Set<String> _nids) {
        news = _news;
        nids = _nids;
    }
    //实现自定义点击事件接口，因为需要判断横竖屏
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    private OnItemClickListener itemClickListener;
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}