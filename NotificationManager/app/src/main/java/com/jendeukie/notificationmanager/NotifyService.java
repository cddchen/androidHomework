package com.jendeukie.notificationmanager;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotifyService extends Service {
    public NotifyService() {
    }
    public class NotifyBinder extends Binder {
        public NotifyService getService() {
            return NotifyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new NotifyBinder();
    }
    public void setTimeByBroadcast(final List<ContentValues> list, final LocalBroadcastManager localBroadcastManager) {
        if (list.size() == 0) return;
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (list.size() == 0) {
                    timer.cancel();
                    return;
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
                Date date = new Date(System.currentTimeMillis());
                String end_time = list.get(0).get("NotifyTime").toString();
                int nID = Integer.parseInt(list.get(0).get("nID").toString());
                String title = list.get(0).get("Title").toString();
                Log.d("Date", simpleDateFormat.format(date) + " " + simpleDateFormat.format(date).compareTo(end_time) + " " + end_time);
                while (end_time.compareTo(simpleDateFormat.format(date)) == -1) {
                    list.remove(0);
                    if (list.size() == 0) return;
                    end_time = list.get(0).get("NotifyTime").toString();
                    nID = Integer.parseInt(list.get(0).get("nID").toString());
                    title = list.get(0).get("Title").toString();
                }
                if (end_time.compareTo(simpleDateFormat.format(date)) == 0) {
                    Intent intent = new Intent("com.jendeukie.notificationmanager.LOCAL_BROADCAST");
                    intent.putExtra("nID", nID);
                    intent.putExtra("Title", title);
                    localBroadcastManager.sendBroadcast(intent);
                    list.remove(0);
                }
            }
        };
        timer.schedule(task, 0, 60 * 1000);
    }
}
