package com.sybiload.elyst.Database.Item;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class HandlerIndex extends SQLiteOpenHelper
{
    public static final String COLUMN_ID_ITEM = "id_item";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_BAR_TYPE = "bar_type";
    public static final String COLUMN_BAR_CODE = "bar_code";

    private static final String DATABASE_NAME = "index";
    private static final int DATABASE_VERSION = 1;

    protected static final String CURRENT_TABL = "ind";
    protected static final String ITEM_CREATE = "CREATE TABLE " + CURRENT_TABL + " (" + COLUMN_ID_ITEM + " TEXT, " + COLUMN_NAME + " TEXT, " + COLUMN_CATEGORY + " INTEGER DEFAULT 0, " + COLUMN_PRICE + " REAL DEFAULT 0.0, " + COLUMN_BAR_TYPE + " TEXT, " + COLUMN_BAR_CODE + " TEXT);";
    protected static final String ITEM_INSERT = "INSERT INTO " + CURRENT_TABL + " VALUES (?, ?, ?, ?, ?, ?);";
    protected static final String ITEM_UPDATE = "UPDATE " + CURRENT_TABL + " SET " + COLUMN_ID_ITEM + "=?, " + COLUMN_NAME + "=?, " + COLUMN_CATEGORY + "=?, " + COLUMN_PRICE + "=?, " + COLUMN_BAR_TYPE + "=?, " + COLUMN_BAR_CODE + "=? WHERE " + COLUMN_ID_ITEM + "=?;";

    public HandlerIndex(Context context)
    {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        // create the root tabl
        database.execSQL(ITEM_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(HandlerIndex.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CURRENT_TABL);
        onCreate(db);
    }
}
