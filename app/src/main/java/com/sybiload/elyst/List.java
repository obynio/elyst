package com.sybiload.elyst;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class List
{
    private String name;
    private String description;
    private String database;
    public static ArrayList<Item> itemShop;
    public static ArrayList<Item> itemAvailable;

    public List(String name, String description, String database)
    {
        this.name = name;
        this.description = description;
        this.database = database;
        itemShop = new ArrayList<Item>();
        itemAvailable = new ArrayList<Item>();
    }

    public String getName()
    {
        return this.name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return this.description;
    }
    public void setDescription (String description)
    {
        this.description= description;
    }

    public String getDatabase()
    {
        return this.database;
    }
    public void setDatabase(String database)
    {
        this.database = database;
    }


    // sort item in alphabetical order
    public void sortShop(Context ctx)
    {
        SharedPreferences mainPref = ctx.getApplicationContext().getSharedPreferences("main", 0);

        if (mainPref.getString("listPreferenceUiShopSort", "alphabetical").equals("alphabetical"))
        {
            Collections.sort(itemShop, new Comparator<Item>()
            {
                public int compare(Item v1, Item v2) {
                    return v1.getName().compareTo(v2.getName());
                }
            });
        }
        else if (mainPref.getString("listPreferenceUiShopSort", "alphabetical").equals("category"))
        {
            Collections.sort(itemShop, new Comparator<Item>()
            {
                public int compare(Item v1, Item v2) {
                    return Integer.toString(v1.getColor()).compareTo(Integer.toString(v2.getColor()));
                }
            });
        }
    }

    // sort item in alphabetical order
    public void sortAvailable(Context ctx)
    {
        SharedPreferences mainPref = ctx.getApplicationContext().getSharedPreferences("main", 0);

        if (mainPref.getString("listPreferenceUiAddSort", "alphabetical").equals("alphabetical"))
        {
            Collections.sort(itemAvailable, new Comparator<Item>()
            {
                public int compare(Item v1, Item v2) {
                    return v1.getName().compareTo(v2.getName());
                }
            });
        }
        else if (mainPref.getString("listPreferenceUiAddSort", "alphabetical").equals("category"))
        {
            Collections.sort(itemAvailable, new Comparator<Item>()
            {
                public int compare(Item v1, Item v2) {
                    return Integer.toString(v1.getColor()).compareTo(Integer.toString(v2.getColor()));
                }
            });
        }
    }

    // sort item in alphabetical order
    public void sortShopDone()
    {
        Collections.sort(itemShop, new Comparator<Item>()
        {
            public int compare(Item v1, Item v2) {
                return v1.isDone().compareTo(v2.isDone());
            }
        });
    }
}