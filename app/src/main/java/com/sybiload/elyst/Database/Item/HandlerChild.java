package com.sybiload.elyst.Database.Item;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class HandlerChild extends SQLiteOpenHelper
{
    public static final String COLUMN_ID_ITEM = "id_item";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_DONE = "done";

    private static final int DATABASE_VERSION = 1;

    protected static final String CURRENT_TABL = "root";
    protected static final String ITEM_CREATE = "CREATE TABLE " + CURRENT_TABL + " (" + COLUMN_ID_ITEM + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_QUANTITY + " REAL DEFAULT 0.0, " + COLUMN_UNIT + " INTEGER DEFAULT 0, " + COLUMN_DONE + " INTEGER DEFAULT 0);";
    protected static final String ITEM_INSERT = "INSERT INTO " + CURRENT_TABL + " VALUES (?, ?, ?, ?, ?);";
    protected static final String ITEM_UPDATE = "UPDATE " + CURRENT_TABL + " SET " + COLUMN_ID_ITEM + "=?, " + COLUMN_DESCRIPTION + "=?, " + COLUMN_QUANTITY + "=?, " + COLUMN_UNIT + "=?, " + COLUMN_DONE + "=? WHERE " + COLUMN_ID_ITEM + "=?;";

    public HandlerChild(Context context, String database)
    {
        super(context, database, null, DATABASE_VERSION);
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
        Log.w(HandlerChild.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CURRENT_TABL);
        onCreate(db);
    }
}
