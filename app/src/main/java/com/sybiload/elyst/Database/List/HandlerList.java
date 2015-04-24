package com.sybiload.elyst.Database.List;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HandlerList extends SQLiteOpenHelper
{
    public static final String COLUMN_ID_DB = "id_db";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_BACKGROUND = "background";

    public static final String CURRENT_TABL = "root";
    private static final String DATABASE_NAME = "main";
    private static final int DATABASE_VERSION = 1;

    private static final String LIST_CREATE = "CREATE TABLE " + CURRENT_TABL + " (" + COLUMN_ID_DB + " TEXT, " + COLUMN_NAME + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_BACKGROUND + " INTEGER DEFAULT 0);";
    protected static final String LIST_INSERT = "INSERT INTO " + CURRENT_TABL + " VALUES (?, ?, ?, ?);";
    protected static final String LIST_UPDATE = "UPDATE " + CURRENT_TABL + " SET " + COLUMN_ID_DB + "=?, " + COLUMN_NAME + "=?, " + COLUMN_DESCRIPTION + "=?, " + COLUMN_BACKGROUND + "=? WHERE " + COLUMN_ID_DB + "=?;";

    public HandlerList(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        // create the root tabl
        database.execSQL(LIST_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(HandlerList.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CURRENT_TABL);
        onCreate(db);
    }
}