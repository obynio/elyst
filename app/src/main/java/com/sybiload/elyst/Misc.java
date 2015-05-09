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
    public static void leftTransition(Activity act)
    {
        act.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public static void rightTransition(Activity act)
    {
        act.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public static void log(String message)
    {
        if (BuildConfig.DEBUG)
        {
            Log.e("com.sybiload.mytag", message);
        }

    }

    public static double pxToDp(final Context context, final double px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static double dpToPx(final Context context, final double dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    // sort list in alphabetical order
    public static void sortList(ArrayList<List> list)
    {
        Collections.sort(list, new Comparator<List>()
        {
            public int compare(List v1, List v2)
            {
                return v1.getName().compareTo(v2.getName());
            }
        });
    }

    public static void populateList(Context ctx)
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

    public static void populateItem(Context ctx, List list)
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

    // get default item and send back an arraylist with all the default items
    public static void populateDefaultItem(Context ctx)
    {
        ArrayList<Item> itemArray = new ArrayList<Item>();
        String[] defaultItemName = ctx.getResources().getStringArray(R.array.item_name);
        int[] defaultItemCategory = ctx.getResources().getIntArray(R.array.item_color);

        DatabaseItem databaseItem = new DatabaseItem(ctx, null);
        databaseItem.open();

        for (int i = 0; i < defaultItemName.length; i++)
        {
            Item myItem = new Item(generateSeed(), defaultItemName[i], null, defaultItemCategory[i], 0.0, 0.0, 0, null, null, false);
            databaseItem.createItem(myItem);
        }

        databaseItem.dbIndex.close();
    }

    public static String generateSeed()
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

    public static void createItem(Context ctx, Item myItem)
    {
        // adding item to the database
        DatabaseItem databaseItem = new DatabaseItem(ctx, Static.currentList.getIdDb());
        databaseItem.open();

        databaseItem.createItem(myItem);

        databaseItem.dbIndex.close();
        databaseItem.dbChild.close();
    }

    public static void insertItem(Context ctx, Item myItem)
    {
        // adding item to the database
        DatabaseItem databaseItem = new DatabaseItem(ctx, Static.currentList.getIdDb());
        databaseItem.open();

        databaseItem.insertItem(myItem);

        databaseItem.dbIndex.close();
        databaseItem.dbChild.close();
    }

    public static void removeItem(Context ctx, Item myItem)
    {
        // removing item from the database
        DatabaseItem databaseItem = new DatabaseItem(ctx, Static.currentList.getIdDb());
        databaseItem.open();

        databaseItem.removeItem(myItem);

        databaseItem.dbIndex.close();
        databaseItem.dbChild.close();
    }

    public static void deleteItem(Context ctx, Item myItem)
    {
        // removing item from the database
        DatabaseItem databaseItem = new DatabaseItem(ctx, Static.currentList.getIdDb());
        databaseItem.open();

        databaseItem.deleteItem(myItem);

        databaseItem.dbIndex.close();
        databaseItem.dbChild.close();
    }

    public static void updateItem(Context ctx, Item myItem)
    {
        // removing item from the database
        DatabaseItem databaseItem = new DatabaseItem(ctx, Static.currentList.getIdDb());
        databaseItem.open();

        databaseItem.updateItem(myItem);

        databaseItem.dbIndex.close();
        databaseItem.dbChild.close();
    }

    public static void createList(Context ctx, List myList)
    {
        // open list tabl database
        DatabaseList databaseList = new DatabaseList(ctx);
        databaseList.open();

        // insert list in the root tabl database
        databaseList.insertList(myList);
        databaseList.dbList.close();

        populateList(ctx);
    }

    public static void updateList(Context ctx, List myList)
    {
        DatabaseList databaseList = new DatabaseList(ctx);
        databaseList.open();

        databaseList.updateByName(myList);

        databaseList.dbList.close();
    }

    public static void deleteList(Context ctx, List myList)
    {
        DatabaseList databaseList = new DatabaseList(ctx);
        databaseList.open();

        databaseList.deleteItem(myList);
        databaseList.dbList.close();
    }

    public static int getColor(Context ctx, int color)
    {
        if (color == 1)
            return ctx.getResources().getColor(R.color.green);
        else if (color == 2)
            return ctx.getResources().getColor(R.color.orange);
        else if (color == 3)
            return ctx.getResources().getColor(R.color.red);
        else if (color == 4)
            return ctx.getResources().getColor(R.color.purple);
        else if (color == 5)
            return ctx.getResources().getColor(R.color.blue);
        else
            return ctx.getResources().getColor(R.color.grey);
    }
}
