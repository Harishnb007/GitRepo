package com.loancare.lakeview.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Dbhelper extends SQLiteOpenHelper {
    static String DATABASE_NAME="sample";
    public static final String TABLE_NAME="user";
    public static final String KEY_UNAME="uname";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_CHECKED="checked";
    public static final String KEY_ENABLE="enable";

    public Dbhelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
        @Override
    public void onCreate(SQLiteDatabase db) {
            String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("
                    + KEY_UNAME    + " TEXT ,"
                    + KEY_PASSWORD + " TEXT ,"
                    + KEY_ENABLE + " TEXT ,"
                    + KEY_CHECKED  + " TEXT " + " ) ";
            db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);
    }
}
