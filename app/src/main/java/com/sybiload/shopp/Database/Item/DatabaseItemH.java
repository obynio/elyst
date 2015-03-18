package com.sybiload.shopp.Database.Item;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseItemH extends SQLiteOpenHelper
{
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_SHOP = "shop";
    public static final String COLUMN_DONE = "done";

    private static final int DATABASE_VERSION = 1;

    protected static final String CURRENT_TABL = "item";
    protected static final String CREATE_ITEM = "CREATE TABLE " + CURRENT_TABL + " (" + COLUMN_NAME + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_ICON + " INTEGER DEFAULT 0, " + COLUMN_SHOP + " INTEGER DEFAULT 0, " + COLUMN_DONE + " INTEGER DEFAULT 0);";

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