package com.sybiload.elyst;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sybiload.elyst.Database.Item.DatabaseItem;
import com.sybiload.elyst.Database.List.DatabaseList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Misc
{
    public void leftTransition(Activity act)
    {
        act.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public void rightTransition(Activity act)
    {
        act.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public void log(String message)
    {
        if (BuildConfig.DEBUG)
        {
            Log.e("com.sybiload.mytag", message);
        }

    }
    // sort list in alphabetical order
    public void sortList(ArrayList<List> list)
    {
        Collections.sort(list, new Comparator<List>()
        {
            public int compare(List v1, List v2)
            {
                return v1.getName().compareTo(v2.getName());
            }
        });
    }

    public void populateList(Context ctx)
    {
        DatabaseList databaseList = new DatabaseList(ctx);

        Static.allList = new ArrayList<List>();

        // read all list in the database and populate static list array
        databaseList.open();
        Static.allList = databaseList.readList();
        databaseList.dbList.close();

        // sort all this stuff
        sortList(Static.allList);
    }

    public void populateItem(Context ctx, List list)
    {
        DatabaseItem databaseItem = new DatabaseItem(ctx, list.getIdDb());
        databaseItem.open();

        // reset list fields
        Static.currentList.itemShop = new ArrayList<Item>();
        Static.currentList.itemAvailable = new ArrayList<Item>();

        // read all item in database
        Static.currentList.itemShop = databaseItem.readChildItem();

        ArrayList<Item> tmpList = databaseItem.readIndexItem();

        // so shitty !
        for (Item tmp : tmpList)
        {
            boolean test = false;

            for (Item tmpbis : Static.currentList.itemShop)
            {
                if (tmp.getIdItem().equals(tmpbis.getIdItem()))
                {
                    test = true;
                    break;
                }
            }

            if (!test)
            {
                Static.currentList.itemAvailable.add(tmp);
            }
        }

        // close databases
        databaseItem.dbIndex.close();
        databaseItem.dbChild.close();
    }

    public String generateSeed()
    {
        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++)
        {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }
    public void addList(Context ctx, List myList)
    {
        // open list tabl database
        DatabaseList databaseList = new DatabaseList(ctx);
        databaseList.open();

        log("creating test list..");

        // insert list in the root tabl database
        databaseList.insertList(myList);
        databaseList.dbList.close();

        populateList(ctx);
    }

    // get default item and send back an arraylist with all the default items
    public void populateDefaultItem(Context ctx)
    {
        ArrayList<Item> itemArray = new ArrayList<Item>();
        String[] defaultItemName = ctx.getResources().getStringArray(R.array.item_name);
        int[] defaultItemCategory = ctx.getResources().getIntArray(R.array.item_color);

        DatabaseItem databaseItem = new DatabaseItem(ctx, null);
        databaseItem.open();

        for (int i = 0; i < defaultItemName.length; i++)
        {
            Item myItem = new Item(generateSeed(), defaultItemName[i], null, defaultItemCategory[i], 0.0, 0.0, null, null, null, false);
            databaseItem.createItem(myItem);
        }

        databaseItem.dbIndex.close();
    }

    public void createItem(Context ctx, Item myItem)
    {
        // adding item to the database
        DatabaseItem databaseItem = new DatabaseItem(ctx, Static.currentList.getIdDb());
        databaseItem.open();

        databaseItem.createItem(myItem);

        databaseItem.dbIndex.close();
        databaseItem.dbChild.close();

        // adding item to the current list
        //populateItem(ctx, Static.currentList);
    }

    public void insertItem(Context ctx, Item myItem)
    {
        // adding item to the database
        DatabaseItem databaseItem = new DatabaseItem(ctx, Static.currentList.getIdDb());
        databaseItem.open();

        databaseItem.insertItem(myItem);

        databaseItem.dbIndex.close();
        databaseItem.dbChild.close();

        // adding item to the current list
        //populateItem(ctx, Static.currentList);
    }

    public void removeItem(Context ctx, Item myItem)
    {
        // removing item from the database
        DatabaseItem databaseItem = new DatabaseItem(ctx, Static.currentList.getIdDb());
        databaseItem.open();

        databaseItem.removeItem(myItem);

        databaseItem.dbIndex.close();
        databaseItem.dbChild.close();

        // removing item from the current list
        //populateItem(ctx, Static.currentList);
    }

    public void deleteItem(Context ctx, Item myItem)
    {
        // removing item from the database
        DatabaseItem databaseItem = new DatabaseItem(ctx, Static.currentList.getIdDb());
        databaseItem.open();

        databaseItem.deleteItem(myItem);

        databaseItem.dbIndex.close();
        databaseItem.dbChild.close();

        // removing item from the current list
        //populateItem(ctx, Static.currentList);
    }

    public void updateItem(Context ctx, Item myItem)
    {
        // removing item from the database
        DatabaseItem databaseItem = new DatabaseItem(ctx, Static.currentList.getIdDb());
        databaseItem.open();

        databaseItem.updateItem(myItem);

        databaseItem.dbIndex.close();
        databaseItem.dbChild.close();

        // removing item from the current list
        //populateItem(ctx, Static.currentList);
    }

    public String getColor(Context ctx, int color)
    {
        if (color == 1)
            return "#" + Integer.toHexString(ctx.getResources().getColor(R.color.green));
        else if (color == 2)
            return "#" + Integer.toHexString(ctx.getResources().getColor(R.color.orange));
        else if (color == 3)
            return "#" + Integer.toHexString(ctx.getResources().getColor(R.color.red));
        else if (color == 4)
            return "#" + Integer.toHexString(ctx.getResources().getColor(R.color.purple));
        else if (color == 5)
            return "#" + Integer.toHexString(ctx.getResources().getColor(R.color.blue));
        else
            return "#9e9e9e";
    }
}
