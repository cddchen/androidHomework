package com.example.tst2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TextView title = (TextView)findViewById(R.id.title);
        title.setText(bundle.getString("title"));
        TextView source = (TextView)findViewById(R.id.source);
        source.setText("来源：" + bundle.getString("source"));
        TextView time = (TextView)findViewById(R.id.time);
        time.setText("时间：" + bundle.getString("time"));
        TextView content = (TextView)findViewById(R.id.detail);
        content.setText(bundle.getString("content"));

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
            }
        };
        Timer timer = new Timer();
        long delay = 30 * 1000;
        timer.schedule(task, delay);
    }
}
