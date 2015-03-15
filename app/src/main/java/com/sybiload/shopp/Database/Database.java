package com.sybiload.shopp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sybiload.shopp.Item;
import com.sybiload.shopp.Static;

public class Database
{
    private SQLiteDatabase database;
    private DatabaseHandler databaseHandler;

    public Database(Context ctx)
    {
        databaseHandler = new DatabaseHandler(ctx);
    }

    public void open() throws SQLException
    {
        database = databaseHandler.getWritableDatabase();
    }

    public void close() {
        databaseHandler.close();
    }


    public void insertItem(Item myItem)
    {
        ContentValues value = new ContentValues();
        value.put(DatabaseHandler.COLUMN_ID, myItem.getName());
        database.insert(DatabaseHandler.TABLE_ITEMS, null, value);
    }

    public void remove(String key, String keyValue)
    {
        database.delete(DatabaseHandler.TABLE_ITEMS, key + " = " + keyValue, null);
    }

    public void updateByName(String name, Item newItem)
    {
        ContentValues value = new ContentValues();
        value.put(DatabaseHandler.COLUMN_ID, newItem.getName());
        value.put(DatabaseHandler.COLUMN_FOOTER, newItem.getFooter());
        value.put(DatabaseHandler.COLUMN_ICON, newItem.getIcon());
        value.put(DatabaseHandler.COLUMN_SHOP, newItem.isToShop() ? 1 : 0);
        value.put(DatabaseHandler.COLUMN_DONE, newItem.isDone() ? 1 : 0);
        database.update(DatabaseHandler.TABLE_ITEMS, value, DatabaseHandler.COLUMN_ID + " = '" + name + "'", null);
    }

    public void readAllItems()
    {
        Cursor c = database.rawQuery("select * from " + DatabaseHandler.TABLE_ITEMS, null);

        while (c.moveToNext())
        {
            Item myItem = new Item(c.getString(0), c.getString(1), c.getInt(2), (c.getInt(3) != 0), (c.getInt(4) != 0));

            if (myItem.isToShop())
            {
                Static.itemShop.add(myItem);
            }
            else
            {
                Static.itemAvailable.add(myItem);
            }
        }

        c.close();
    }
}
