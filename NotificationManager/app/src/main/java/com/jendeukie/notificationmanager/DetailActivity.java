package com.jendeukie.notificationmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("To Do.");
        toolbar.setSubtitle("提醒事项");
        setSupportActionBar(toolbar);

        TextView title = (TextView)findViewById(R.id.detail_txt_title),
                notify = (TextView)findViewById(R.id.detail_txt_notify),
                deadtime = (TextView)findViewById(R.id.detail_txt_deadtime);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        title.setText(bundle.getString("Title"));
        int is_notify = bundle.getInt("isNotify");
        if (is_notify == 1)
            notify.setText(bundle.getString("NotifyTime"));
        int is_deadtime = bundle.getInt("isDeadTime");
        if (is_deadtime == 1)
            deadtime.setText(bundle.getString("DeadTime"));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "任务完成了", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });
    }
}
