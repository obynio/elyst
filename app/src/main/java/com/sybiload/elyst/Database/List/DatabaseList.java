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

public class DatabaseList
{
    private HandlerList handlerList;
    public SQLiteDatabase dbList;

    Context ctx;

    public DatabaseList(Context ctx)
    {
        this.ctx = ctx;

        handlerList = new HandlerList(ctx);
    }

    // allow to open list tabl
    public void open() throws SQLException
    {
        dbList = handlerList.getWritableDatabase();
    }

    public void deleteItem(List newList)
    {
        dbList.delete(HandlerList.CURRENT_TABL, HandlerList.COLUMN_ID_DB + "='" + newList.getIdDb() + "'", null);
    }

    // insert a new list into the table
    public void insertList(List newList)
    {
        SQLiteStatement stmtList = dbList.compileStatement(HandlerList.LIST_INSERT);
        stmtList.bindString(1, newList.getIdDb());
        stmtList.bindString(2, newList.getName());

        if (newList.getDescription() == null)
            stmtList.bindNull(3);
        else
            stmtList.bindString(3, newList.getDescription());

        stmtList.execute();
    }

    public void updateByName(List newList)
    {
        SQLiteStatement stmtList = dbList.compileStatement(HandlerList.LIST_UPDATE);
        stmtList.bindString(1, newList.getIdDb());
        stmtList.bindString(2, newList.getName());

        if (newList.getDescription() == null)
            stmtList.bindNull(3);
        else
            stmtList.bindString(3, newList.getDescription());

        // value to search old list
        stmtList.bindString(4, newList.getIdDb());

        stmtList.execute();
    }

    // read all list from the table
    public ArrayList<List> readList()
    {
        ArrayList<List> allList = new ArrayList<List>();
        // get all the rows
        Cursor c = dbList.rawQuery("SELECT * FROM " + HandlerList.CURRENT_TABL, null);

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