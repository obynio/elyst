package com.sybiload.shopp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

        // set temp default hashSet
        Set<String> defaultAvailableItems = new HashSet<String>(Arrays.asList(ctx.getResources().getStringArray(R.array.items_available)));
        Set<String> defaultShopItems = new HashSet<String>();

        // get saved items and put them into hashSet
        Set<String> myAvailableItems = itemPref.getStringSet("itemAvailable", defaultAvailableItems);
        Set<String> myShopItems = itemPref.getStringSet("itemShop", defaultShopItems);

        // for each availableItem, put it into availableItem array
        for (String str : myAvailableItems)
            Static.itemAvailable.add(new Item(str));

        // for each availableItem, put it into shopItem array
        for (String str : myShopItems)
            Static.itemShop.add(new Item(str));

        // sort all this stuff
        sortItems(Static.itemAvailable);
        sortItems(Static.itemShop);
    }
}
