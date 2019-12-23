package com.example.tst8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView display_txt, time_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display_txt = (TextView)findViewById(R.id.display_txt);
        time_edit = (TextView)findViewById(R.id.time_edit);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        time_edit.setText(simpleDateFormat.format(new Date(System.currentTimeMillis())));
        Button confirmBt = (Button)findViewById(R.id.confirm_bt);
        confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClockService.class);
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            ClockService clockService = ((ClockService.ClockBinder)service).getService();
            clockService.SetOnTimeUpListener(new ClockService.OnTimeUpListener() {
                @Override
                public void onTimeUp() {
                    Log.d("ClockService", "TimeUp Bind Success");
                    display_txt.setText("时间到！");
                    display_txt.setTextColor(Color.RED);
                }
            });
            clockService.setTime(time_edit.getText().toString());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
