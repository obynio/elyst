package com.sybiload.shopp.Database.Item;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.sybiload.shopp.Item;

import java.util.ArrayList;

public class DatabaseItem extends DatabaseItemH
{
    private SQLiteDatabase database;

    Context ctx;

    public DatabaseItem(Context ctx, String name)
    {
        super(ctx, name);
        this.ctx = ctx;
    }

    // allow to open list tabl
    public void open() throws SQLException
    {
        database = getWritableDatabase();
    }

    // insert a new list into the table
    public void deleteItem(Item newItem)
    {
        database.delete(CURRENT_TABL, COLUMN_NAME + "='" + newItem.getName() + "'", null);
    }

    // insert a new list into the table
    public void insertItem(Item newItem)
    {
        SQLiteStatement stmt = database.compileStatement(INSERT_INTO);
        stmt.bindString(1, newItem.getName());

        if (newItem.getDescription() == null)
            stmt.bindNull(2);
        else
            stmt.bindString(2, newItem.getDescription());

        stmt.bindLong(3, newItem.getIcon());

        if (newItem.getBarType() == null)
            stmt.bindNull(4);
        else
            stmt.bindString(4, newItem.getBarType());

        if (newItem.getBarCode() == null)
            stmt.bindNull(5);
        else
            stmt.bindString(5, newItem.getBarCode());

        stmt.bindLong(6, newItem.isToShop() ? 1 : 0);
        stmt.bindLong(7, newItem.isDone() ? 1 : 0);
        stmt.execute();
    }

    public void updateByName(String name, Item newItem)
    {
        SQLiteStatement stmt = database.compileStatement(UPDATE);
        stmt.bindString(1, newItem.getName());

        if (newItem.getDescription() == null)
            stmt.bindNull(2);
        else
            stmt.bindString(2, newItem.getDescription());

        stmt.bindLong(3, newItem.getIcon());

        if (newItem.getBarType() == null)
            stmt.bindNull(4);
        else
            stmt.bindString(4, newItem.getBarType());

        if (newItem.getBarCode() == null)
            stmt.bindNull(5);
        else
            stmt.bindString(5, newItem.getBarCode());

        stmt.bindLong(6, newItem.isToShop() ? 1 : 0);
        stmt.bindLong(7, newItem.isDone() ? 1 : 0);
        stmt.bindString(8, name);
        stmt.execute();
    }

    public ArrayList<Item> searchItem(String s)
    {
        // get all the rows
        ArrayList<Item> arrayItem = new ArrayList<Item>();

        // only select the row which have the first letters of the name in common and that are not available to shop
        Cursor c = database.rawQuery("SELECT * FROM " + CURRENT_TABL + " WHERE name LIKE '" + s + "%' AND shop LIKE 0", null);

        while (c.moveToNext())
        {
            // for each row, create a new list object and add it to the list array
            Item myItem = new Item(c.getString(0), c.getString(1), c.getInt(2), c.getString(3), c.getString(4), (c.getInt(5) != 0), (c.getInt(6) != 0));
            arrayItem.add(myItem);
        }

        // return the result of the research
        c.close();
        return arrayItem;
    }

    // read all list from the table
    public ArrayList<Item> readAllItem()
    {
        // get all the rows
        ArrayList<Item> arrayItem = new ArrayList<Item>();
        Cursor c = database.rawQuery("SELECT * FROM " + CURRENT_TABL, null);

        while (c.moveToNext())
        {
            // for each row, create a new list object and add it to the list array
            Item myItem = new Item(c.getString(0), c.getString(1), c.getInt(2), c.getString(3), c.getString(4), (c.getInt(5) != 0), (c.getInt(6) != 0));
            arrayItem.add(myItem);
        }

        c.close();
        return arrayItem;
    }
}