package com.example.tst2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context mcontext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table NewsTable (" +
                "nID integer primary key autoincrement, " +
                "Title text, " +
                "Content text, " +
                "Source text, " +
                "time text" +
                ")");
        ContentValues cv = new ContentValues();
        cv.put("Title", "习近平全面依法治国新理念新思想新战略十论");
        cv.put("Content", "党的十八大以来，以习近平同志为核心的党中央提出一系列全面依法治国新理念新思想新战略，概括起来有十个方面的重要内容。习近平指出，这些新理念新思想新战略，是马克思主义法治思想中国化的最新成果，是全面依法治国的根本遵循，必须长期坚持、不断丰富发展。在第六个国家宪法日之际，新华社《学习进行时》为您梳理。");
        cv.put("Source", "新华网");
        cv.put("time", "2019-12-04");
        db.insert("NewsTable", null, cv);
        cv.clear();
        cv.put("Title", "医疗扶贫，照亮贫困家庭的明天");
        cv.put("Content", "国家卫生健康委员会相关负责人介绍，未来将深入推进县医院能力建设、“县乡一体、乡村一体”机制建设和乡村医疗卫生机构标准化建设，到2019年底基本消除乡村医疗卫生机构和人员“空白点”，到2020年全面完成解决基本医疗有保障存在的突出问题，不断提高贫困群众的健康获得感和满意度。");
        cv.put("Source", "新华网");
        cv.put("time", "2019-12-02");
        db.insert("NewsTable", null, cv);
        for (int i = 0; i < 5; ++i) {
            cv.clear();
            cv.put("Title", "习近平全面依法治国新理念新思想新战略十论");
            cv.put("Content", "党的十八大以来，以习近平同志为核心的党中央提出一系列全面依法治国新理念新思想新战略，概括起来有十个方面的重要内容。习近平指出，这些新理念新思想新战略，是马克思主义法治思想中国化的最新成果，是全面依法治国的根本遵循，必须长期坚持、不断丰富发展。在第六个国家宪法日之际，新华社《学习进行时》为您梳理。");
            cv.put("Source", "新华网");
            cv.put("time", "2019-12-04");
            db.insert("NewsTable", null, cv);
            cv.clear();
            cv.put("Title", "医疗扶贫，照亮贫困家庭的明天");
            cv.put("Content", "国家卫生健康委员会相关负责人介绍，未来将深入推进县医院能力建设、“县乡一体、乡村一体”机制建设和乡村医疗卫生机构标准化建设，到2019年底基本消除乡村医疗卫生机构和人员“空白点”，到2020年全面完成解决基本医疗有保障存在的突出问题，不断提高贫困群众的健康获得感和满意度。");
            cv.put("Source", "新华网");
            cv.put("time", "2019-12-02");
            db.insert("NewsTable", null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
