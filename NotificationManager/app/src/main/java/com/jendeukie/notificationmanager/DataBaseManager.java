package com.jendeukie.notificationmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseManager extends SQLiteOpenHelper {
    private Context mcontext;

    public DataBaseManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table ItemList (" +
                "nID integer primary key autoincrement," +
                "Title text, " +
                "Category int," +
                "isNotify tinyint," +
                "NotifyTime text," +
                "isDeadTime tinyint," +
                "DeadTime text)");

        ContentValues cv = new ContentValues();
        cv.put("Title", "我的第一个提醒事项");
        cv.put("Category", 1);
        cv.put("isNotify", 1);
        cv.put("NotifyTime", "2020-06-04 10:40");
        cv.put("isDeadTime", 0);
        db.insert("ItemList", null, cv);

        db.execSQL("create table CategoryList(" +
                "nID integer primary key autoincrement," +
                "Name text)");
        ContentValues category_default = new ContentValues();
        category_default.put("Name", "个人");
        db.insert("CategoryList", null, category_default);
        category_default.clear();
        category_default.put("Name", "工作");
        db.insert("CategoryList", null, category_default);
        category_default.clear();
        category_default.put("Name", "学习");
        db.insert("CategoryList", null, category_default);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
