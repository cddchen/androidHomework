package com.example.tst2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NewsAdapter adapter;
    Set<String> readlistdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取sqlite中新闻，装入news中
        MyDatabaseHelper dhelper = new MyDatabaseHelper(this, "news.db", null, 1);
        SQLiteDatabase db = dhelper.getWritableDatabase();
        Cursor cursor = db.query("NewsTable", null, null, null, null, null, null);
        final List<News> news = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                News news1 = new News();
                news1.Title = cursor.getString(cursor.getColumnIndex("Title"));
                news1.Source = cursor.getString(cursor.getColumnIndex("Source"));
                news1.Content = cursor.getString(cursor.getColumnIndex("Content"));
                news1.Time = cursor.getString(cursor.getColumnIndex("time"));
                news.add(news1);
            } while (cursor.moveToNext());
        }
        //获取sharedprefences中的已读条目，装入readlistdata中
        SharedPreferences pref = getSharedPreferences("readlist", MODE_PRIVATE);
        readlistdata = pref.getStringSet("nID",  new HashSet<String>());

        recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(news, readlistdata);
        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Configuration mConfiguration = MainActivity.this.getResources().getConfiguration();
                Bundle bundle = new Bundle();
                bundle.putString("title", news.get(position).Title);
                bundle.putString("source", news.get(position).Source);
                bundle.putString("content", news.get(position).Content);
                bundle.putString("time", news.get(position).Time);
                if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {
                    DetailFragment fragment = (DetailFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_detail);
                    fragment.Refresh(bundle);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            adapter.Read((NewsAdapter.ViewHolder)recyclerView.findViewHolderForAdapterPosition(requestCode));
            Log.d("hadreadnid", "" + requestCode);
            readlistdata.add(Integer.toString(requestCode));
            SharedPreferences.Editor editor = getSharedPreferences("readlist", MODE_PRIVATE).edit();
            editor.remove("nID");
            editor.putStringSet("nID", readlistdata);
            editor.apply();
        }
    }
}
