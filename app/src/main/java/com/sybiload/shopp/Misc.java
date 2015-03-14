package com.sybiload.shopp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    public void populateItems(Context ctx)
    {
        SharedPreferences itemPref = ctx.getSharedPreferences("item", 0);

        Set<String> defaultAvailableItems = new HashSet<String>(Arrays.asList(ctx.getResources().getStringArray(R.array.items_available)));
        Set<String> defaultShopItems = new HashSet<String>();

        Static.itemAvailable = new ArrayList<String>(itemPref.getStringSet("itemAvailable", defaultAvailableItems));
        Static.itemShop = new ArrayList<String>(itemPref.getStringSet("itemShop", defaultShopItems));

        Collections.sort(Static.itemAvailable);
        Collections.sort(Static.itemShop);
    }
}
