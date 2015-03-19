package com.sybiload.shopp.Database.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sybiload.shopp.Database.Item.DatabaseItem;
import com.sybiload.shopp.List;
import com.sybiload.shopp.Misc;
import com.sybiload.shopp.Static;

public class DatabaseList extends DatabaseListH
{
    private SQLiteDatabase database;

    Context ctx;

    public DatabaseList(Context ctx)
    {
        super(ctx);
        this.ctx = ctx;
    }

    // allow to open list tabl
    public void open() throws SQLException
    {
        database = getWritableDatabase();
    }

    // insert a new list into the table
    public void insertList(List newList)
    {
        ContentValues value = new ContentValues();
        value.put(COLUMN_NAME, newList.getName());
        value.put(COLUMN_DESCRIPTION, newList.getDescription());
        value.put(COLUMN_TABL, newList.getDatabase());
        database.insert(CURRENT_TABL, null, value);
    }

    // read all list from the table
    public void readAllList()
    {
        // get all the rows
        Cursor c = database.rawQuery("select * from " + CURRENT_TABL, null);

        while (c.moveToNext())
        {
            // for each row, create a new list object and add it to the list array
            List myList = new List(c.getString(0), c.getString(1), c.getString(2));

            DatabaseItem databaseItem = new DatabaseItem(ctx, myList.getDatabase());
            databaseItem.open();

            myList.setItem(databaseItem.readAllItem());

            databaseItem.close();

            Static.allList.add(myList);
        }

        c.close();
    }
}