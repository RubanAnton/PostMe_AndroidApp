package com.example.antonruban.postme.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by antonruban on 25.05.17.
 */

public class MyOpenHelper extends SQLiteOpenHelper {
    public MyOpenHelper(Context context) {
        super(context, MySchema.DATABASE_NAME, null, MySchema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MySchema.UserTable.NAME
                + "(_id integer primary key autoincrement, "
                + MySchema.UserTable.Cols.EMAIL + ", "
                + MySchema.UserTable.Cols.USERNAME + ", "
                + MySchema.UserTable.Cols.PASSWORD + ")");

        db.execSQL("create table " + MySchema.PostTable.NAME
                + "(_id integer primary key autoincrement, "
                + MySchema.PostTable.Cols.USERNAME + ", "
                + MySchema.PostTable.Cols.POST + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
