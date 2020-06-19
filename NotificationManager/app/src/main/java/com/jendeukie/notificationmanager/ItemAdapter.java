package com.jendeukie.notificationmanager;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<ItemDetail> items;
    private Map<Integer, String> categorys;
    public ItemAdapter(List<ItemDetail> _items, Map<Integer, String> _categorys) {
        items = _items;
        categorys = _categorys;
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_layout, parent, false);
        final ItemViewHolder holder = new ItemViewHolder(itemView);
        if (itemClickListener != null) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    itemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemDetail detail = items.get(position);
        holder.txt_title.setText(detail.Title);
        holder.txt_category.setText(categorys.get(detail.Category));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
        String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        if (detail.isDeadTime == 1) {
            holder.txt_deadtime.setText(detail.DeadTime);
            if (detail.DeadTime.compareTo(date) <= 0)
                holder.txt_deadtime.setTextColor(Color.RED);
            holder.img_deadtime.setVisibility(View.VISIBLE);
        }
        else {
            holder.txt_deadtime.setVisibility(View.INVISIBLE);
            holder.img_deadtime.setVisibility(View.INVISIBLE);
        }
        if (detail.isNotify == 1) {
            holder.txt_notify.setText(detail.NotifyTime);
            if (detail.NotifyTime.compareTo(date) <= 0)
                holder.txt_notify.setTextColor(Color.RED);
            holder.img_notify.setVisibility(View.VISIBLE);
        }
        else {
            holder.txt_notify.setVisibility(View.INVISIBLE);
            holder.img_notify.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_title, txt_category, txt_notify, txt_deadtime;
        private ImageView img_notify, img_deadtime;
        private CardView cardView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_category = itemView.findViewById(R.id.txt_category);
            txt_notify = itemView.findViewById(R.id.txt_notify_of_itemview_layout);
            txt_deadtime = itemView.findViewById(R.id.txt_deadtime_of_itemview_layout);
            img_notify = itemView.findViewById(R.id.img_notify);
            img_deadtime = itemView.findViewById(R.id.img_deadtime);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    private OnItemClickListener itemClickListener;
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
