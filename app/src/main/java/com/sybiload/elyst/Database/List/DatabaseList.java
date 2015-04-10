package com.sybiload.elyst.Database.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.sybiload.elyst.Database.Item.DatabaseItem;
import com.sybiload.elyst.Item;
import com.sybiload.elyst.List;
import com.sybiload.elyst.Misc;
import com.sybiload.elyst.Static;

import java.util.ArrayList;

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

    public void deleteItem(List newList)
    {
        database.delete(CURRENT_TABL, COLUMN_TABL + "='" + newList.getDatabase() + "'", null);
    }

    // insert a new list into the table
    public void insertList(List newList)
    {
        SQLiteStatement stmt = database.compileStatement(INSERT_INTO);
        stmt.bindString(1, newList.getName());

        if (newList.getDescription() == null)
            stmt.bindNull(2);
        else
            stmt.bindString(2, newList.getDescription());

        stmt.bindString(3, newList.getDatabase());
        stmt.execute();
    }

    public void updateByName(String name, List newList)
    {
        SQLiteStatement stmt = database.compileStatement(UPDATE);
        stmt.bindString(1, newList.getName());

        if (newList.getDescription() == null)
            stmt.bindNull(2);
        else
            stmt.bindString(2, newList.getDescription());

        stmt.bindString(3, newList.getDatabase());
        stmt.bindString(4, name);
        stmt.execute();
    }

    // read all list from the table
    public ArrayList<List> readAllList()
    {
        ArrayList<List> allList = new ArrayList<List>();
        // get all the rows
        Cursor c = database.rawQuery("SELECT * FROM " + CURRENT_TABL, null);

        while (c.moveToNext())
        {
            // for each row, create a new list object and add it to the list array
            List myList = new List(c.getString(0), c.getString(1), c.getString(2));

            allList.add(myList);
        }

        c.close();
        return allList;
    }
}