package com.sybiload.shopp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

    // sort item in alphabetical order
    public void sortItem(ArrayList<Item> item)
    {
        Collections.sort(item, new Comparator<Item>()
        {
            public int compare(Item v1, Item v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
    }

    public void testList(Context ctx)
    {
        // open list tabl database
        DatabaseList databaseList = new DatabaseList(ctx);
        databaseList.open();

        log("creating test list..");

        // create test list
        List testList = new List("hey", "world", "hey.db");

        // fill it with all default item
        testList.setItem(populateDefaultItem(ctx));



        // insert list in the root tabl database
        databaseList.insertList(testList);
        databaseList.close();

        DatabaseItem databaseItem = new DatabaseItem(ctx, testList.getDatabase());
        databaseItem.open();
        //databaseItem.createNewTabl();

        for (Item newItem : testList.getItem())
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
        Set<String> defaultAvailableItems = new HashSet<String>(Arrays.asList(ctx.getResources().getStringArray(R.array.items_available)));

        for (String str : defaultAvailableItems)
        {
            Item myItem = new Item(str, null, R.mipmap.ic_launcher, false, false);
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
        databaseList.readAllList();
        databaseList.close();

        // sort all this stuff
        sortList(Static.allList);
    }
}
