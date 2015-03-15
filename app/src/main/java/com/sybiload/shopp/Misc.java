package com.sybiload.shopp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.util.Log;

import com.sybiload.shopp.Database.Database;
import com.sybiload.shopp.Database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Misc
{
    public void log(String message)
    {
        if (BuildConfig.DEBUG)
        {
            Log.e("com.sybiload.shop.log", message);
        }

    }

    public void leftTransition(Activity act)
    {
        SharedPreferences readPref = PreferenceManager.getDefaultSharedPreferences(act);

        if (readPref.getBoolean("checkBoxUiPrefTransitions", true))
        {
            act.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
    }

    public void rightTransition(Activity act)
    {
        SharedPreferences readPref = PreferenceManager.getDefaultSharedPreferences(act);

        if (readPref.getBoolean("checkBoxUiPrefTransitions", true))
        {
            act.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    }

    public void sortItems(ArrayList<Item> list)
    {
        Collections.sort(list, new Comparator<Item>()
        {
            public int compare(Item v1, Item v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
    }

    public void populateItems(Context ctx)
    {
        SharedPreferences itemPref = ctx.getSharedPreferences("item", 0);

        Database database = new Database(ctx);
        database.open();

        // set temp default hashSet
        Set<String> defaultAvailableItems = new HashSet<String>(Arrays.asList(ctx.getResources().getStringArray(R.array.items_available)));
        Set<String> defaultShopItems = new HashSet<String>();

        boolean firstLaunch = itemPref.getBoolean("firstLaunch", true);

        if (firstLaunch)
        {
            itemPref.edit().putBoolean("firstLaunch", false).commit();

            log("firstLaunch, creating database..");

            for (String str : defaultAvailableItems)
            {
                Item myItem = new Item(str, null, R.mipmap.ic_launcher, false, false);
                database.insertItem(myItem);
            }
        }

        log("populate itemArrays from database..");

        database.readAllItems();
        database.close();

        // sort all this stuff
        sortItems(Static.itemAvailable);
        sortItems(Static.itemShop);
    }
}
