package com.example.tst8;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ClockService extends Service {

    public ClockService() {
    }

    public class ClockBinder extends Binder {
        public ClockService getService() {
            return ClockService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return new ClockBinder();
    }

    //利用回调接口实现服务和窗体的交互
    private OnTimeUpListener IOnTimeUpListener;
    public interface OnTimeUpListener {
        void onTimeUp();
    }

    public void SetOnTimeUpListener(OnTimeUpListener onTimeUpListener) {
        IOnTimeUpListener = onTimeUpListener;
    }

    public void setTime(final String end_time) {
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");// HH:mm:ss
                Date date = new Date(System.currentTimeMillis());
                Log.d("Date", simpleDateFormat.format(date) + " <> " + end_time);
                if (end_time.equals(simpleDateFormat.format(date))) {
                    if (IOnTimeUpListener != null) {
                        IOnTimeUpListener.onTimeUp();
                    }
                    timer.cancel();
                }
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    //利用广播实现与窗体交互
    public void setTimeByBroadcast(final String end_time, final LocalBroadcastManager localBroadcastManager) {
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");// HH:mm:ss
                Date date = new Date(System.currentTimeMillis());
                Log.d("Date", simpleDateFormat.format(date) + " <> " + end_time);
                if (end_time.equals(simpleDateFormat.format(date))) {
                    Intent intent = new Intent("com.example.tst8.LOCAL_BROADCAST");
                    intent.putExtra("Result", "Time_Up");
                    localBroadcastManager.sendBroadcast(intent);
                    timer.cancel();
                }
            }
        };
        timer.schedule(task, 1000, 1000);
    }
}
