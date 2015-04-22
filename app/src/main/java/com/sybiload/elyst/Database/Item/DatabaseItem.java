package com.sybiload.elyst.Database.Item;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.sybiload.elyst.Item;
import com.sybiload.elyst.List;
import com.sybiload.elyst.Static;

import java.util.ArrayList;

public class DatabaseItem
{
    HandlerIndex handlerIndex;
    public SQLiteDatabase dbIndex;

    HandlerChild handlerChild;
    public SQLiteDatabase dbChild;

    Context ctx;
    String idDb;

    public DatabaseItem(Context ctx, String idDb)
    {
        this.ctx = ctx;

        handlerIndex = new HandlerIndex(ctx);

        this.idDb = idDb;

        if (this.idDb != null)
            handlerChild = new HandlerChild(ctx, idDb);
    }

    // allow to open list tabl
    public void open() throws SQLException
    {
        dbIndex = handlerIndex.getWritableDatabase();

        if (this.idDb != null)
            dbChild = handlerChild.getWritableDatabase();
    }

    // delete item from all databases
    public void deleteItem(Item newItem)
    {
        dbChild.close();

        for (List mylist : Static.allList)
        {
            HandlerChild handlerCurrent = new HandlerChild(ctx, mylist.getIdDb());
            SQLiteDatabase dbCurrent = handlerCurrent.getWritableDatabase();
            dbCurrent.delete(HandlerChild.CURRENT_TABL, HandlerChild.COLUMN_ID_ITEM + "='" + newItem.getIdItem() + "'", null);
        }

        dbIndex.delete(HandlerIndex.CURRENT_TABL, HandlerIndex.COLUMN_ID_ITEM + "='" + newItem.getIdItem() + "'", null);
    }

    // remove item from current list
    public void removeItem(Item newItem)
    {
        dbChild.delete(HandlerChild.CURRENT_TABL, HandlerChild.COLUMN_ID_ITEM + "='" + newItem.getIdItem() + "'", null);
    }

    // create a new item into the table
    public void createItem(Item newItem)
    {
        // insert into HandlerIndex database
        SQLiteStatement stmtIndex = dbIndex.compileStatement(HandlerIndex.ITEM_INSERT);

        stmtIndex.bindString(1, newItem.getIdItem());
        stmtIndex.bindString(2, newItem.getName());
        stmtIndex.bindLong(3, newItem.getCategory());
        stmtIndex.bindDouble(4, newItem.getPrice());

        if (newItem.getBarType() == null)
            stmtIndex.bindNull(5);
        else
            stmtIndex.bindString(5, newItem.getBarType());

        if (newItem.getBarCode() == null)
            stmtIndex.bindNull(6);
        else
            stmtIndex.bindString(6, newItem.getBarCode());

        // execute all this stuff
        stmtIndex.execute();
    }

    // insert a new item into the table
    public void insertItem(Item newItem)
    {
        // insert HandlerChild database
        SQLiteStatement stmtChild = dbChild.compileStatement(HandlerChild.ITEM_INSERT);
        stmtChild.bindString(1, newItem.getIdItem());

        if (newItem.getDescription() == null)
            stmtChild.bindNull(2);
        else
            stmtChild.bindString(2, newItem.getDescription());

        stmtChild.bindDouble(3, newItem.getQuantity());

        if (newItem.getUnit() == null)
            stmtChild.bindNull(4);
        else
            stmtChild.bindString(4, newItem.getUnit());

        stmtChild.bindLong(5, newItem.getDone() ? 1 : 0);

        // execute all this stuff
        stmtChild.execute();
    }

    public void updateItem(Item newItem)
    {
        // update HandlerIndex database
        SQLiteStatement stmtIndex = dbIndex.compileStatement(HandlerIndex.ITEM_UPDATE);

        stmtIndex.bindString(1, newItem.getIdItem());
        stmtIndex.bindString(2, newItem.getName());
        stmtIndex.bindLong(3, newItem.getCategory());
        stmtIndex.bindDouble(4, newItem.getPrice());

        if (newItem.getBarType() == null)
            stmtIndex.bindNull(5);
        else
            stmtIndex.bindString(5, newItem.getBarType());

        if (newItem.getBarCode() == null)
            stmtIndex.bindNull(6);
        else
            stmtIndex.bindString(6, newItem.getBarCode());

        // value to search old item
        stmtIndex.bindString(7, newItem.getIdItem());

        // update HandlerChild database
        SQLiteStatement stmtChild = dbChild.compileStatement(HandlerChild.ITEM_UPDATE);
        stmtChild.bindString(1, newItem.getIdItem());

        if (newItem.getDescription() == null)
            stmtChild.bindNull(2);
        else
            stmtChild.bindString(2, newItem.getDescription());

        stmtChild.bindDouble(3, newItem.getQuantity());

        if (newItem.getUnit() == null)
            stmtChild.bindNull(4);
        else
            stmtChild.bindString(4, newItem.getUnit());

        stmtChild.bindLong(5, newItem.getDone() ? 1 : 0);

        // value to search old item
        stmtChild.bindString(6, newItem.getIdItem());

        // execute all this stuff
        stmtIndex.execute();
        stmtChild.execute();
    }

    public ArrayList<Item> searchItem(String s)
    {
        // get all the rows
        ArrayList<Item> arrayItem = new ArrayList<Item>();

        // only select the row which have the first letters of the name in common and that are not available to shop
        Cursor cIndex = dbIndex.rawQuery("SELECT * FROM " + HandlerIndex.CURRENT_TABL + " WHERE name LIKE '" + s + "%'", null);

        while (cIndex.moveToNext())
        {
            // for each row, create a new list object and add it to the list array
            Item myItem = new Item(cIndex.getString(0), cIndex.getString(1), null, cIndex.getInt(2), cIndex.getDouble(3), 0.0, null, cIndex.getString(4), cIndex.getString(5), false);
            arrayItem.add(myItem);
        }

        // return the result of the research
        cIndex.close();
        return arrayItem;
    }

    // read index item from database
    public ArrayList<Item> readIndexItem()
    {
        // get all the rows
        ArrayList<Item> arrayItem = new ArrayList<Item>();
        Cursor cIndex = dbIndex.rawQuery("SELECT * FROM " + HandlerIndex.CURRENT_TABL, null);

        while (cIndex.moveToNext())
        {
            // for each row, create a new list object and add it to the list array
            Item myItem = new Item(cIndex.getString(0), cIndex.getString(1), null, cIndex.getInt(2), cIndex.getDouble(3), 0.0, null, cIndex.getString(4), cIndex.getString(5), false);
            arrayItem.add(myItem);
        }

        cIndex.close();
        return arrayItem;
    }

    // read child item from database
    public ArrayList<Item> readChildItem()
    {
        // get all the rows
        ArrayList<Item> arrayItem = new ArrayList<Item>();
        Cursor cChild = dbChild.rawQuery("SELECT * FROM " + HandlerChild.CURRENT_TABL, null);

        while (cChild.moveToNext())
        {
            String hllo = cChild.getString(0);
            Cursor cIndex = dbIndex.rawQuery("SELECT * FROM " + HandlerIndex.CURRENT_TABL + " WHERE id_item='" + cChild.getString(0) + "'", null);
            cIndex.moveToFirst();

            // for each row, create a new list object and add it to the list array
            Item myItem = new Item(cChild.getString(0), cIndex.getString(1), cChild.getString(1), cIndex.getInt(2), cIndex.getDouble(3), cChild.getDouble(2), cChild.getString(3), cIndex.getString(4), cIndex.getString(5), cChild.getInt(4) != 0);
            arrayItem.add(myItem);

            cIndex.close();
        }


        cChild.close();
        return arrayItem;
    }
}
