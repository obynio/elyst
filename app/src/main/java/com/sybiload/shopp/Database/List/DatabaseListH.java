package com.sybiload.shopp.Database.List;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseListH extends SQLiteOpenHelper
{
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TABL = "tabl";

    public static final String CURRENT_TABL = "root";
    private static final String DATABASE_NAME = "main.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_LIST = "CREATE TABLE " + CURRENT_TABL + " (" + COLUMN_NAME + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_TABL + " TEXT);";

    public DatabaseListH(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        // create the root tabl
        database.execSQL(CREATE_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(DatabaseListH.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CURRENT_TABL);
        onCreate(db);
    }

}