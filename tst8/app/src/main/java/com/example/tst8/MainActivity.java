package com.example.tst8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static final int CHANGE = 1;

    private EditText editText;
    private Timer timer;
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case CHANGE:
                    //start();
                    if (editText.getText().equals("0"))
                        timer.cancel();
                    int now = Integer.parseInt(editText.getText().toString()) - 1;
                    editText.setText(Integer.toString(now));
                    break;
                default:
                    break;
            }
        }
    };

    private void timer_start() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = CHANGE;
                handler.sendMessage(message);
            }
        };
        timer = new Timer();
        timer.schedule(task, 1000, 1000);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.editText);
        editText.setText("100");

        final Button startbt = (Button)findViewById(R.id.start);
        startbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer_start();
            }
        });
        Button endbt = (Button)findViewById(R.id.end);
        endbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
            }
        });
    }
}
