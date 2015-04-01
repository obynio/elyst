package com.sybiload.shopp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sybiload.shopp.Database.Item.DatabaseItem;
import com.sybiload.shopp.Database.List.DatabaseList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
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

    public void addList(Context ctx, List myList)
    {
        // open list tabl database
        DatabaseList databaseList = new DatabaseList(ctx);
        databaseList.open();

        log("creating test list..");

        // fill it with all default item
        myList.itemAvailable = populateDefaultItem(ctx);



        // insert list in the root tabl database
        databaseList.insertList(myList);
        databaseList.close();

        DatabaseItem databaseItem = new DatabaseItem(ctx, myList.getDatabase());
        databaseItem.open();
        //databaseItem.createNewTabl();

        for (Item newItem : myList.itemAvailable)
        {
            databaseItem.insertItem(newItem);
        }

        databaseItem.close();

        populateList(ctx);
    }

    // get default item and send back an arraylist with all the default items
    public ArrayList<Item> populateDefaultItem(Context ctx)
    {
        ArrayList<Item> itemArray = new ArrayList<Item>();
        String[] defaultItemName = ctx.getResources().getStringArray(R.array.item_name);
        int[] defaultItemColor = ctx.getResources().getIntArray(R.array.item_color);

        for (int i = 0; i < defaultItemName.length; i++)
        {
            Item myItem = new Item(defaultItemName[i], null, defaultItemColor[i], null, null, false, false);
            itemArray.add(myItem);
        }

        return itemArray;
    }

    public void populateList(Context ctx)
    {
        DatabaseList databaseList = new DatabaseList(ctx);

        Static.allList = new ArrayList<List>();

        // read all list in the database and populate static list array
        databaseList.open();
        Static.allList = databaseList.readAllList();
        databaseList.close();

        // sort all this stuff
        sortList(Static.allList);
    }

    public void populateItem(Context ctx, List list)
    {
        DatabaseItem databaseItem = new DatabaseItem(ctx, list.getDatabase());
        databaseItem.open();

        // reset list fields
        Static.currentList.itemShop = new ArrayList<Item>();
        Static.currentList.itemAvailable = new ArrayList<Item>();

        // read all item in database
        ArrayList<Item> allItem = databaseItem.readAllItem();


        // sort items depending if they are to shop or not
        for (Item it : allItem)
        {
            if (it.isToShop())
            {
                Static.currentList.itemShop.add(it);
            }
            else
            {
                Static.currentList.itemAvailable.add(it);
            }
        }

        /*
        // sort items in the list
        Static.currentList.sortAvailable();
        Static.currentList.sortShop();
        Static.currentList.sortShopDone();
        */

        databaseItem.close();
    }
}
