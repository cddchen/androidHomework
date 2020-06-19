package com.jendeukie.notificationmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private List<ItemDetail> items;
    private Map<Integer, String> categorys;
    private Map<String, Integer> categorys_Name2nID;
    private List<String> categorys_strings;
    private ItemAdapter adapter;
    private DataBaseManager dbManager;
    private LocalBroadcastManager localBroadcastManager;
    private List<ContentValues> notifyList;
    private ServiceConnection connection;
    private NotificationManager notificationManager;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ToDo.");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });


        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        LayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(LayoutManager);
        notifyList = new ArrayList<>();
        try {
            InitData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        notifyList.sort(new NotifyCompartor());
        adapter = new ItemAdapter(items, categorys);
        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Title", items.get(position).Title);
                bundle.putInt("isNotify", items.get(position).isNotify);
                if (items.get(position).isNotify == 1)
                    bundle.putString("NotifyTime", items.get(0).NotifyTime);
                bundle.putInt("isDeadTime", items.get(position).isDeadTime);
                if (items.get(position).isDeadTime == 1)
                    bundle.putString("DeadTime", items.get(0).DeadTime);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.jendeukie.notificationmanager.LOCAL_BROADCAST");
        LocalReceiver localReceiver = new LocalReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                NotifyService notifySerive = ((NotifyService.NotifyBinder)service).getService();
                notifySerive.setTimeByBroadcast(notifyList, localBroadcastManager);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(MainActivity.this, NotifyService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private class NotifyCompartor implements Comparator<ContentValues> {
        @Override
        public int compare(ContentValues o1, ContentValues o2) {
            return o1.get("NotifyTime").toString().compareTo(o2.get("NotifyTime").toString());
        }
    }
    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }

    private void InitData() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        dbManager = new DataBaseManager(this, "items.db", null, 1);
        SQLiteDatabase db = dbManager.getWritableDatabase();
        Cursor cursor = db.query("ItemList", null, null, null, null, null, null);
        items = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ItemDetail item = new ItemDetail();
                item.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("nID")));
                item.Title = cursor.getString(cursor.getColumnIndex("Title"));
                item.Category = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Category")));
                item.isDeadTime = Integer.parseInt(cursor.getString(cursor.getColumnIndex("isDeadTime")));
                if (item.isDeadTime == 1)
                    item.DeadTime = cursor.getString(cursor.getColumnIndex("DeadTime"));
                item.isNotify = Integer.parseInt(cursor.getString(cursor.getColumnIndex("isNotify")));
                if (item.isNotify == 1) {
                    //过滤掉已经过期的通知
                    item.NotifyTime = cursor.getString(cursor.getColumnIndex("NotifyTime"));
                    if (item.NotifyTime.compareTo(simpleDateFormat.format(date)) >= 0) {
                        ContentValues cv = new ContentValues();
                        cv.put("nID", item.id);
                        cv.put("Title", item.Title);
                        cv.put("NotifyTime", item.NotifyTime);
                        notifyList.add(cv);
                    }
                }
                //Log.d("item", item.id + " " + item.Title + " " + item.Category + " " + item.isNotify + " " + item.isDeadTime);
                items.add(item);
            } while (cursor.moveToNext());
        }
        db.close();

        dbManager = new DataBaseManager(this, "items.db", null, 1);
        db = dbManager.getWritableDatabase();
        cursor = db.query("CategoryList", null, null, null, null, null, null);
        categorys = new HashMap<>();
        categorys_Name2nID = new HashMap<>();
        categorys_strings = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int nID = Integer.parseInt(cursor.getString(cursor.getColumnIndex("nID")));
                String Name = cursor.getString(cursor.getColumnIndex("Name"));
                Log.d("category_list", nID + " " + Name);
                categorys.put(nID, Name);
                categorys_Name2nID.put(Name, nID);
                categorys_strings.add(Name);
            } while (cursor.moveToNext());
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addItem(ContentValues cv) {
        SQLiteDatabase db = dbManager.getWritableDatabase();
        int id = (int) db.insert("ItemList", null, cv);
        db.close();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());

        //Log.d("insert_id", id + "");
        ItemDetail item = new ItemDetail();
        item.id = id;
        item.Title = cv.get("Title").toString();
        item.Category = Integer.parseInt(cv.get("Category").toString());
        item.isDeadTime = Integer.parseInt(cv.get("isDeadTime").toString());
        if (item.isDeadTime == 1)
            item.DeadTime = cv.get("DeadTime").toString();
        item.isNotify = Integer.parseInt(cv.get("isNotify").toString());
        if (item.isNotify == 1) {
            item.NotifyTime = cv.get("NotifyTime").toString();
            if (item.NotifyTime.compareTo(simpleDateFormat.format(date)) >= 0) {
                ContentValues tmp = new ContentValues();
                tmp.put("nID", item.id);
                tmp.put("Title", item.Title);
                tmp.put("NotifyTime", item.NotifyTime);
                notifyList.add(tmp);

                notifyList.sort(new NotifyCompartor());
                unbindService(connection);
                Intent intent = new Intent(MainActivity.this, NotifyService.class);
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
            }
        }
        items.add(item);
        adapter.notifyItemInserted(items.size());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //新增事项弹窗
    private void showBottomDialog(){
        final Dialog dialog = new Dialog(this,R.style.DialogTheme);
        View view = View.inflate(this,R.layout.dialog_layout,null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.main_menu_animStyle);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        final Spinner spinner = dialog.findViewById(R.id.spinner_of_dialog_layout);
        ArrayAdapter<String> spinner_adpter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, categorys_strings);

        spinner.setAdapter(spinner_adpter);
        final EditText editText_title = dialog.findViewById(R.id.dialog_editxt_title);
        final TextView Dialog_txt_notify = dialog.findViewById(R.id.dialog_txt_notify);
        Dialog_txt_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出选择时间
                final Dialog dialogDatePicker = new Dialog(MainActivity.this, R.style.DialogTheme);
                View view = View.inflate(MainActivity.this ,R.layout.layout_dialog_datepicker,null);
                final DatePicker datePicker = view.findViewById(R.id.datepicker);
                final TimePicker timePicker = view.findViewById(R.id.timepicker);
                TextView cancel = view.findViewById(R.id.txt_cancel_of_layoutdialogdatepicker);
                TextView confirm = view.findViewById(R.id.txt_confirm_of_layoutdialogdatepicker);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        int year = datePicker.getYear(), month = datePicker.getMonth() + 1, day = datePicker.getDayOfMonth();
                        int hour = timePicker.getHour(), minute = timePicker.getMinute();
                        Dialog_txt_notify.setText(year + "-" + (month <= 9 ? "0" + month : month) + "-" + (day <= 9 ? "0" + day : day) + " " + (hour <= 9 ? "0" + hour : hour) + ":" + (minute <= 9 ? "0" + minute : minute));
                        dialogDatePicker.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog_txt_notify.setText("提醒时间");
                        dialogDatePicker.dismiss();
                    }
                });
                dialogDatePicker.setContentView(view);
                Window window = dialogDatePicker.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.main_menu_animStyle);
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialogDatePicker.show();
            }
        });
        final TextView Dialog_txt_deadtime = (TextView)dialog.findViewById(R.id.dialog_txt_deadtime);
        Dialog_txt_deadtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogDatePicker = new Dialog(MainActivity.this, R.style.DialogTheme);
                View view = View.inflate(MainActivity.this ,R.layout.layout_dialog_datepicker,null);
                final DatePicker datePicker = view.findViewById(R.id.datepicker);
                final TimePicker timePicker = view.findViewById(R.id.timepicker);
                TextView cancel = view.findViewById(R.id.txt_cancel_of_layoutdialogdatepicker);
                TextView confirm = view.findViewById(R.id.txt_confirm_of_layoutdialogdatepicker);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        //SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        int year = datePicker.getYear(), month = datePicker.getMonth() + 1, day = datePicker.getDayOfMonth();
                        int hour = timePicker.getHour(), minute = timePicker.getMinute();
                        Dialog_txt_deadtime.setText(year + "-" + (month <= 9 ? "0" + month : month) + "-" + (day <= 9 ? "0" + day : day) + " " + (hour <= 9 ? "0" + hour : hour) + ":" + (minute <= 9 ? "0" + minute : minute));
                        dialogDatePicker.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog_txt_deadtime.setText("结束时间");
                        dialogDatePicker.dismiss();
                    }
                });
                dialogDatePicker.setContentView(view);
                Window window = dialogDatePicker.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.main_menu_animStyle);
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialogDatePicker.show();
            }
        });
        dialog.findViewById(R.id.txt_confirm_of_layoutdialog).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String title = editText_title.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(MainActivity.this, "提醒事项不能为空！", Toast.LENGTH_SHORT).show();
                }
                else {
                    ContentValues cv = new ContentValues();
                    cv.put("Title", title);
                    cv.put("Category", categorys_Name2nID.get(spinner.getSelectedItem().toString()));
                    if (Dialog_txt_notify.getText().toString().equals("提醒时间"))
                        cv.put("isNotify", 0);
                    else {
                        cv.put("isNotify", 1);
                        cv.put("NotifyTime", Dialog_txt_notify.getText().toString());
                    }
                    if (Dialog_txt_deadtime.getText().toString().equals("结束时间"))
                        cv.put("isDeadTime", 0);
                    else {
                        cv.put("isDeadTime", 1);
                        cv.put("DeadTime", Dialog_txt_deadtime.getText().toString());
                    }
                    addItem(cv);
                    dialog.dismiss();
                }
            }
        });
    }
    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int nid = intent.getIntExtra("nID", 0);
            String title = intent.getStringExtra("Title");
            //Log.d("broadcast", nid + " " + title);
            NotificationUtils notificationUtils = new NotificationUtils(context);
            notificationUtils.sendNotification("提醒事项", title + " 需要完成了！");
        }
    }
}