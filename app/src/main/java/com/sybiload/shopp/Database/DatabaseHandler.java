package com.sybiload.shopp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper
{

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FOOTER = "footer";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_SHOP = "shop";
    public static final String COLUMN_DONE = "done";

    public static final String TABLE_ITEMS = "items";
    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_ITEMS + " (" + COLUMN_ID + " TEXT, " + COLUMN_FOOTER + " TEXT, " + COLUMN_ICON + " INTEGER DEFAULT 0, " + COLUMN_SHOP + " INTEGER DEFAULT 0, " + COLUMN_DONE + " INTEGER DEFAULT 0);";

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(DatabaseHandler.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

}