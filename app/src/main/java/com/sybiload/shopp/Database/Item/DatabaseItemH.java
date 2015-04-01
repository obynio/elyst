package com.sybiload.shopp.Database.Item;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseItemH extends SQLiteOpenHelper
{
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_BAR_TYPE = "bar_type";
    public static final String COLUMN_BAR_CODE = "bar_code";
    public static final String COLUMN_SHOP = "shop";
    public static final String COLUMN_DONE = "done";

    private static final int DATABASE_VERSION = 1;

    protected static final String CURRENT_TABL = "item";
    protected static final String CREATE_ITEM = "CREATE TABLE " + CURRENT_TABL + " (" + COLUMN_NAME + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_COLOR + " INTEGER DEFAULT 0, " + COLUMN_BAR_TYPE + " TEXT, " + COLUMN_BAR_CODE + " TEXT, " + COLUMN_SHOP + " INTEGER DEFAULT 0, " + COLUMN_DONE + " INTEGER DEFAULT 0);";
    protected static final String INSERT_INTO = "INSERT INTO " + CURRENT_TABL + " VALUES (?, ?, ?, ?, ?, ?, ?);";
    protected static final String UPDATE = "UPDATE " + CURRENT_TABL + " SET " + COLUMN_NAME + "=?, " + COLUMN_DESCRIPTION + "=?, " + COLUMN_COLOR + "=?, " + COLUMN_BAR_TYPE + "=?, " + COLUMN_BAR_CODE + "=?, " + COLUMN_SHOP + "=?, " + COLUMN_DONE + "=? WHERE " + COLUMN_NAME + "=?;";

    public DatabaseItemH(Context context, String database)
    {
        super(context, database, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        // create the root tabl
        database.execSQL(CREATE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(DatabaseItemH.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CURRENT_TABL);
        onCreate(db);
    }

}